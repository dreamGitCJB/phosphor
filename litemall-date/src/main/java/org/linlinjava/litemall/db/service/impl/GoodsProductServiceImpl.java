package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.GoodsProduct;
import org.linlinjava.litemall.db.mapper.GoodsProductMapper;
import org.linlinjava.litemall.db.service.IGoodsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品货品表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class GoodsProductServiceImpl extends ServiceImpl<GoodsProductMapper, GoodsProduct> implements IGoodsProductService {
    @Override
    public int reduceStock(Integer id, Integer num) {
        return this.baseMapper.reduceStock(id, num);
    }

    @Override
    public boolean addStock(Integer id, Integer num) {
        int i = this.baseMapper.addStock(id, num);
        return i> 0 ? true : false;
    }

    @Override
    public List<GoodsProduct> queryByGid(Integer goodsId) {
        List<GoodsProduct> goodsProducts = this.list(new LambdaQueryWrapper<GoodsProduct>().eq(GoodsProduct::getGoodsId, goodsId));

        return goodsProducts;
    }
}
