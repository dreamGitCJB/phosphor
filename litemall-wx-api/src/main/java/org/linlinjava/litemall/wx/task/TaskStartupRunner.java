package org.linlinjava.litemall.wx.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.core.task.TaskService;
import org.linlinjava.litemall.db.common.util.OrderUtil;
import org.linlinjava.litemall.db.entity.Order;
import org.linlinjava.litemall.db.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TaskStartupRunner implements ApplicationRunner {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>().eq(Order::getOrderStatus, OrderUtil.STATUS_CREATE));
        for(Order order : orderList){
            LocalDateTime add = order.getAddTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expire =  add.plusMinutes(SystemConfig.getOrderUnpaid());
            if(expire.isBefore(now)) {
                // 已经过期，则加入延迟队列
                taskService.addTask(new OrderUnpaidTask(order.getId(), 0));
            }
            else{
                // 还没过期，则加入延迟队列
                long delay = ChronoUnit.MILLIS.between(now, expire);
                taskService.addTask(new OrderUnpaidTask(order.getId(), delay));
            }
        }
    }
}