package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linlinjava.litemall.db.common.util.OrderUtil;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Order;
import org.linlinjava.litemall.db.mapper.OrderMapper;
import org.linlinjava.litemall.db.service.IOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Override
    public boolean updateWithOptimisticLocker(Order order) {
        LocalDateTime preUpdateTime = order.getUpdateTime();
        order.setUpdateTime(LocalDateTime.now());
        int i = this.baseMapper.updateWithOptimisticLocker(preUpdateTime, order);
        return i > 0 ? true : false;
    }

    @Override
    public IPage<Order> queryByOrderStatus(Integer userId, List<Short> orderStatus, Integer current, Integer limit, String sort, String order) {

        Page page = new Page(current, limit);
        PageUtil.pagetoPage(page,sort,order);
        IPage orderPage = this.page(page, new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId).in(orderStatus != null, Order::getOrderStatus, orderStatus));
        return orderPage;
    }

    @Override
    public Order findByUserIdAndId(Integer userId, Integer orderId) {
        return this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId).eq(Order::getId, orderId));
    }

    @Override
    public boolean updateAftersaleStatus(Integer orderId, Integer status) {
        Order order = new Order();
        order.setId(orderId);
        order.setAftersaleStatus(status);
        boolean b = this.updateById(order);
        return b;
    }

    @Override
    public Map<Object, Object> orderInfo(Integer userId) {

        List<Order> orders = this.list(new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId));


        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (Order order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        orderInfo.put("unship", unship);
        orderInfo.put("unrecv", unrecv);
        orderInfo.put("uncomment", uncomment);
        return orderInfo;

    }
}
