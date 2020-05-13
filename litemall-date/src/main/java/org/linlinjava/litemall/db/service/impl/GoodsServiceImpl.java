package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.mapper.GoodsMapper;
import org.linlinjava.litemall.db.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 商品基本信息表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Override
    public IPage<Goods> querySelective(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order) {

        Page page = new Page(offset, limit);

        PageUtil.pagetoPage(page, sort, order);

        IPage<Goods> goodsIPage = this.page(page, new LambdaQueryWrapper<Goods>()
                .eq(!StringUtils.isEmpty(catId) && catId != 0, Goods::getCategoryId, catId)
                .eq(!StringUtils.isEmpty(brandId), Goods::getBrandId, brandId)
                .eq(!StringUtils.isEmpty(isNew), Goods::getIsNew, isNew)
                .eq(!StringUtils.isEmpty(isHot), Goods::getIsHot, isHot)
                .and(!StringUtils.isEmpty(keywords),i-> i.like(Goods::getKeywords, keywords)).or(i -> i.like(Goods::getName, keywords))
                .eq(Goods::getIsOnSale, true));
        return goodsIPage;


    }

    @Override
    public IPage<Goods> querySelective(Integer goodsId, String goodsSn, String name, Integer current, Integer size, String sort, String order) {

        Page page = new Page(current, size);

        PageUtil.pagetoPage(page, sort, order);

        IPage<Goods> goodsIPage = this.page(page, new LambdaQueryWrapper<Goods>()
                .eq(goodsId != null, Goods::getId, goodsId)
                .eq(!StringUtils.isEmpty(goodsSn), Goods::getGoodsSn, goodsSn)
                .like(!StringUtils.isEmpty(name), Goods::getName, name));

        return goodsIPage;
    }

    /**
     * 获取分类下的商品
     *
     * @param catList
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public IPage<Goods> queryByCategory(List<Integer> catList, int offset, int limit) {
        Page page = new Page(offset, limit);

        return this.page(page, new LambdaQueryWrapper<Goods>().in(Goods::getCategoryId, catList).eq(Goods::getIsOnSale, true));
    }

    /**
     * 获取分类下的商品
     *
     * @param catId
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public IPage<Goods> queryByCategory(Integer catId, int offset, int limit) {

        Page page = new Page(offset, limit);

        PageUtil.pagetoPage(page, "desc", "add_time");

        return this.page(page, new LambdaQueryWrapper<Goods>().eq(Goods::getCategoryId, catId).eq(Goods::getIsOnSale, true));
    }

}
