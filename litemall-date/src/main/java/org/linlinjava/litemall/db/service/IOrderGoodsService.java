package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.OrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IOrderGoodsService extends IService<OrderGoods> {
    List<OrderGoods> queryByOid(Integer orderId);
}
