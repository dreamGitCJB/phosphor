package org.linlinjava.litemall.admin.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.db.common.util.OrderUtil;
import org.linlinjava.litemall.db.entity.Order;
import org.linlinjava.litemall.db.entity.OrderGoods;
import org.linlinjava.litemall.db.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测订单状态
 */
@Component
public class OrderJob {
    private final Log logger = LogFactory.getLog(OrderJob.class);

    @Autowired
    private IOrderGoodsService orderGoodsService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGoodsProductService productService;
    @Autowired
    private IGrouponService grouponService;
    @Autowired
    private IGrouponRulesService rulesService;

    /**
     * 自动确认订单
     * <p>
     * 定时检查订单未确认情况，如果超时 LITEMALL_ORDER_UNCONFIRM 天则自动确认订单
     * 定时时间是每天凌晨3点。
     * <p>
     * TODO
     * 注意，因为是相隔一天检查，因此导致订单真正超时时间是 [LITEMALL_ORDER_UNCONFIRM, 1 + LITEMALL_ORDER_UNCONFIRM]
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkOrderUnconfirm() {
        logger.info("系统开启定时任务检查订单是否已经超期自动确认收货");


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(SystemConfig.getOrderUnconfirm());
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>().eq(Order::getOrderStatus, OrderUtil.STATUS_SHIP)
                .lt(Order::getShipTime, expired));
        for (Order order : orderList) {

            // 设置订单已取消状态
            order.setOrderStatus(OrderUtil.STATUS_AUTO_CONFIRM);
            order.setConfirmTime(LocalDateTime.now());
            if (!orderService.updateWithOptimisticLocker(order)) {
                logger.info("订单 ID=" + order.getId() + " 数据已经更新，放弃自动确认收货");
            } else {
                logger.info("订单 ID=" + order.getId() + " 已经超期自动确认收货");
            }
        }

        logger.info("系统结束定时任务检查订单是否已经超期自动确认收货");
    }

    /**
     * 可评价订单商品超期
     * <p>
     * 定时检查订单商品评价情况，如果确认商品超时 LITEMALL_ORDER_COMMENT 天则取消可评价状态
     * 定时时间是每天凌晨4点。
     * <p>
     * TODO
     * 注意，因为是相隔一天检查，因此导致订单真正超时时间是 [LITEMALL_ORDER_COMMENT, 1 + LITEMALL_ORDER_COMMENT]
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void checkOrderComment() {
        logger.info("系统开启任务检查订单是否已经超期未评价");


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(SystemConfig.getOrderComment());

        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>().gt(Order::getComments, 0)
                .lt(Order::getShipTime, expired));
        for (Order order : orderList) {
            order.setComments(0);
            orderService.updateWithOptimisticLocker(order);

            List<OrderGoods> orderGoodsList = orderGoodsService.queryByOid(order.getId());
            for (OrderGoods orderGoods : orderGoodsList) {
                orderGoods.setComment(-1);
                orderGoodsService.updateById(orderGoods);
            }
        }

        logger.info("系统结束任务检查订单是否已经超期未评价");
    }
}
