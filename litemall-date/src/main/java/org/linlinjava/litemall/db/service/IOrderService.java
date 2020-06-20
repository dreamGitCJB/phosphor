package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IOrderService extends IService<Order> {

    IPage<Order> queryByOrderStatus(Integer userId, List<Integer> orderStatus, Integer current, Integer limit, String sort, String order);

    boolean updateWithOptimisticLocker(Order order);

    Order findByUserIdAndId(Integer userId, Integer orderId);


    boolean updateAftersaleStatus(Integer orderId, Integer status);

    Map<Object, Object> orderInfo(Integer userId);
}
