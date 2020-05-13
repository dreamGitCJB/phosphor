package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.OrderGoods;
import org.linlinjava.litemall.db.mapper.OrderGoodsMapper;
import org.linlinjava.litemall.db.service.IOrderGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements IOrderGoodsService {
    @Override
    public List<OrderGoods> queryByOid(Integer orderId) {
        List<OrderGoods> list = this.list(new LambdaQueryWrapper<OrderGoods>().eq(OrderGoods::getOrderId, orderId));
        return list;
    }
}
