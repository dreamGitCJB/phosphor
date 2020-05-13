package org.linlinjava.litemall.wx.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.core.task.Task;
import org.linlinjava.litemall.core.util.BeanUtil;
import org.linlinjava.litemall.db.common.util.OrderUtil;
import org.linlinjava.litemall.db.entity.Order;
import org.linlinjava.litemall.db.entity.OrderGoods;
import org.linlinjava.litemall.db.service.IGoodsProductService;
import org.linlinjava.litemall.db.service.IOrderGoodsService;
import org.linlinjava.litemall.db.service.IOrderService;

import java.time.LocalDateTime;
import java.util.List;

public class OrderUnpaidTask extends Task {
    private final Log logger = LogFactory.getLog(OrderUnpaidTask.class);
    private int orderId = -1;

    public OrderUnpaidTask(Integer orderId, long delayInMilliseconds){
        super("OrderUnpaidTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    public OrderUnpaidTask(Integer orderId){
        super("OrderUnpaidTask-" + orderId, SystemConfig.getOrderUnpaid() * 60 * 1000);
        this.orderId = orderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---订单超时未付款---" + this.orderId);

        IOrderService orderService = BeanUtil.getBean(IOrderService.class);
        IOrderGoodsService orderGoodsService = BeanUtil.getBean(IOrderGoodsService.class);
        IGoodsProductService productService = BeanUtil.getBean(IGoodsProductService.class);

        Order order = orderService.getById(this.orderId);
        if(order == null){
            return;
        }
        if(!OrderUtil.isCreateStatus(order)){
            return;
        }

        // 设置订单已取消状态
        order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
        order.setEndTime(LocalDateTime.now());
        if (!orderService.updateWithOptimisticLocker(order)) {
            throw new RuntimeException("更新数据已失效");
        }

        // 商品货品数量增加
        Integer orderId = order.getId();
        List<OrderGoods> orderGoodsList = orderGoodsService.list(new LambdaQueryWrapper<OrderGoods>().eq(OrderGoods::getOrderId, order));
        for (OrderGoods orderGoods : orderGoodsList) {
            Integer productId = orderGoods.getProductId();
            Integer number = orderGoods.getNumber();
            if (!productService.addStock(productId, number)) {
                throw new RuntimeException("商品货品库存增加失败");
            }
        }
        logger.info("系统结束处理延时任务---订单超时未付款---" + this.orderId);
    }
}
