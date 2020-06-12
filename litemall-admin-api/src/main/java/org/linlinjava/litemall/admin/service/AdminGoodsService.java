package org.linlinjava.litemall.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.dto.GoodsAllinone;
import org.linlinjava.litemall.admin.vo.CatVo;
import org.linlinjava.litemall.core.qcode.QCodeService;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.db.entity.*;
import org.linlinjava.litemall.db.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.linlinjava.litemall.admin.util.AdminResponseCode.GOODS_NAME_EXIST;

@Service
public class AdminGoodsService {
    private final Log logger = LogFactory.getLog(AdminGoodsService.class);

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IGoodsSpecificationService specificationService;
    @Autowired
    private IGoodsAttributeService attributeService;
    @Autowired
    private IGoodsProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private QCodeService qCodeService;

    public Object list(Integer goodsId, String goodsSn, String name,
                       Integer page, Integer limit, String sort, String order) {
        IPage<Goods> goodsList = goodsService.querySelective(goodsId, goodsSn, name, page, limit, sort, order);
        return ResponseUtil.okPageList(goodsList);
    }

    private Object validate(GoodsAllinone goodsAllinone) {
        Goods goods = goodsAllinone.getGoods();
        String name = goods.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String goodsSn = goods.getGoodsSn();
        if (StringUtils.isEmpty(goodsSn)) {
            return ResponseUtil.badArgument();
        }
        // 品牌商可以不设置，如果设置则需要验证品牌商存在
        Integer brandId = goods.getBrandId();
        if (brandId != null && brandId != 0) {
            if (brandService.getById(brandId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }
        // 分类可以不设置，如果设置则需要验证分类存在
        Integer categoryId = goods.getCategoryId();
        if (categoryId != null && categoryId != 0) {
            if (categoryService.getById(categoryId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }

        GoodsAttribute[] attributes = goodsAllinone.getAttributes();
        for (GoodsAttribute attribute : attributes) {
            String attr = attribute.getAttribute();
            if (StringUtils.isEmpty(attr)) {
                return ResponseUtil.badArgument();
            }
            String value = attribute.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        GoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        for (GoodsSpecification specification : specifications) {
            String spec = specification.getSpecification();
            if (StringUtils.isEmpty(spec)) {
                return ResponseUtil.badArgument();
            }
            String value = specification.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        GoodsProduct[] products = goodsAllinone.getProducts();
        for (GoodsProduct product : products) {
            Integer number = product.getNumber();
            if (number == null || number < 0) {
                return ResponseUtil.badArgument();
            }

            BigDecimal price = product.getPrice();
            if (price == null) {
                return ResponseUtil.badArgument();
            }

            String[] productSpecifications = product.getSpecifications();
            if (productSpecifications.length == 0) {
                return ResponseUtil.badArgument();
            }
        }

        return null;
    }

    /**
     * 编辑商品
     *
     * NOTE：
     * 由于商品涉及到四个表，特别是litemall_goods_product表依赖litemall_goods_specification表，
     * 这导致允许所有字段都是可编辑会带来一些问题，因此这里商品编辑功能是受限制：
     * （1）litemall_goods表可以编辑字段；
     * （2）litemall_goods_specification表只能编辑pic_url字段，其他操作不支持；
     * （3）litemall_goods_product表只能编辑price, number和url字段，其他操作不支持；
     * （4）litemall_goods_attribute表支持编辑、添加和删除操作。
     *
     * NOTE2:
     * 前后端这里使用了一个小技巧：
     * 如果前端传来的update_time字段是空，则说明前端已经更新了某个记录，则这个记录会更新；
     * 否则说明这个记录没有编辑过，无需更新该记录。
     *
     * NOTE3:
     * （1）购物车缓存了一些商品信息，因此需要及时更新。
     * 目前这些字段是goods_sn, goods_name, price, pic_url。
     * （2）但是订单里面的商品信息则是不会更新。
     * 如果订单是未支付订单，此时仍然以旧的价格支付。
     */
    @Transactional
    public Object update(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        Goods goods = goodsAllinone.getGoods();
        GoodsAttribute[] attributes = goodsAllinone.getAttributes();
        GoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        GoodsProduct[] products = goodsAllinone.getProducts();

        //将生成的分享图片地址写入数据库
        String url = qCodeService.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
        goods.setShareUrl(url);

        // 商品表里面有一个字段retailPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (GoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);

        // 商品基本信息表litemall_goods
        if (!goodsService.updateById(goods)) {
            throw new RuntimeException("更新数据失败");
        }

        Integer gid = goods.getId();

        // 商品规格表litemall_goods_specification
        for (GoodsSpecification specification : specifications) {
            // 目前只支持更新规格表的图片字段
            if(specification.getUpdateTime() == null){
                specification.setSpecification(null);
                specification.setValue(null);
                specificationService.updateById(specification);
            }
        }

        // 商品货品表litemall_product
        for (GoodsProduct product : products) {
            if(product.getUpdateTime() == null) {
                productService.updateById(product);
            }
        }

        // 商品参数表litemall_goods_attribute
        for (GoodsAttribute attribute : attributes) {
            if (attribute.getId() == null || attribute.getId().equals(0)){
                attribute.setGoodsId(goods.getId());
                attributeService.save(attribute);
            }
            else if(attribute.getDeleted()){
                attributeService.removeById(attribute.getId());
            }
            else if(attribute.getUpdateTime() == null){
                attributeService.updateById(attribute);
            }
        }

        // 这里需要注意的是购物车litemall_cart有些字段是拷贝商品的一些字段，因此需要及时更新
        // 目前这些字段是goods_sn, goods_name, price, pic_url
        for (GoodsProduct product : products) {

            Cart cart = new Cart();
            cart.setPrice(product.getPrice());
            cart.setPicUrl(product.getUrl());
            cart.setGoodsSn(goods.getGoodsSn());
            cart.setGoodsName(goods.getName());

            cartService.update(cart, new LambdaQueryWrapper<Cart>().eq(Cart::getProductId, product.getId()));
        }

        return ResponseUtil.ok();
    }

    @Transactional
    public Object delete(Goods goods) {
        Integer id = goods.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        Integer gid = goods.getId();
        goodsService.removeById(gid);
        specificationService.remove(new LambdaQueryWrapper<GoodsSpecification>().eq(GoodsSpecification::getGoodsId, gid));
        attributeService.remove(new LambdaQueryWrapper<GoodsAttribute>().eq(GoodsAttribute::getGoodsId, gid));
        productService.remove(new LambdaQueryWrapper<GoodsProduct>().eq(GoodsProduct::getGoodsId, gid));

        return ResponseUtil.ok();
    }

    @Transactional
    public Object create(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        Goods goods = goodsAllinone.getGoods();
        GoodsAttribute[] attributes = goodsAllinone.getAttributes();
        GoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        GoodsProduct[] products = goodsAllinone.getProducts();

        String name = goods.getName();
        int count = goodsService.count(new LambdaQueryWrapper<Goods>().eq(Goods::getIsOnSale, true).eq(Goods::getName, name));
        if (count > 0) {
            return ResponseUtil.fail(GOODS_NAME_EXIST, "商品名已经存在");
        }

        // 商品表里面有一个字段retailPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (GoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);

        // 商品基本信息表litemall_goods
        goodsService.save(goods);

        //将生成的分享图片地址写入数据库
        String url = qCodeService.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
        if (!StringUtils.isEmpty(url)) {
            goods.setShareUrl(url);
            if (!goodsService.updateById(goods)) {
                throw new RuntimeException("更新数据失败");
            }
        }

        // 商品规格表litemall_goods_specification
        for (GoodsSpecification specification : specifications) {
            specification.setGoodsId(goods.getId());
            specificationService.save(specification);
        }

        // 商品参数表litemall_goods_attribute
        for (GoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attributeService.save(attribute);
        }

        // 商品货品表litemall_product
        for (GoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            productService.save(product);
        }
        return ResponseUtil.ok();
    }

    public Object list2() {
        // http://element-cn.eleme.io/#/zh-CN/component/cascader
        // 管理员设置“所属分类”
        List<Category> l1CatList = categoryService.queryL1();
        List<CatVo> categoryList = new ArrayList<>(l1CatList.size());

        for (Category l1 : l1CatList) {
            CatVo l1CatVo = new CatVo();
            l1CatVo.setValue(l1.getId());
            l1CatVo.setLabel(l1.getName());

            List<Category> l2CatList = categoryService.queryByPid(l1.getId());
            List<CatVo> children = new ArrayList<>(l2CatList.size());
            for (Category l2 : l2CatList) {
                CatVo l2CatVo = new CatVo();
                l2CatVo.setValue(l2.getId());
                l2CatVo.setLabel(l2.getName());
                children.add(l2CatVo);
            }
            l1CatVo.setChildren(children);

            categoryList.add(l1CatVo);
        }

        // http://element-cn.eleme.io/#/zh-CN/component/select
        // 管理员设置“所属品牌商”
        List<Brand> list = brandService.list();
        List<Map<String, Object>> brandList = new ArrayList<>(l1CatList.size());
        for (Brand brand : list) {
            Map<String, Object> b = new HashMap<>(2);
            b.put("value", brand.getId());
            b.put("label", brand.getName());
            brandList.add(b);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("categoryList", categoryList);
        data.put("brandList", brandList);
        return ResponseUtil.ok(data);
    }

    public Object detail(Integer id) {
        Goods goods = goodsService.getById(id);
        List<GoodsProduct> products = productService.queryByGid(id);
        List<GoodsSpecification> specifications = specificationService.list(new LambdaQueryWrapper<GoodsSpecification>().eq(GoodsSpecification::getGoodsId, id));
        List<GoodsAttribute> attributes = attributeService.list(new LambdaQueryWrapper<GoodsAttribute>().eq(GoodsAttribute::getGoodsId, id));

        Integer categoryId = goods.getCategoryId();
        Category category = categoryService.getById(categoryId);
        Integer[] categoryIds = new Integer[]{};
        if (category != null) {
            Integer parentCategoryId = category.getPid();
            categoryIds = new Integer[]{parentCategoryId, categoryId};
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goods", goods);
        data.put("specifications", specifications);
        data.put("products", products);
        data.put("attributes", attributes);
        data.put("categoryIds", categoryIds);

        return ResponseUtil.ok(data);
    }

}
