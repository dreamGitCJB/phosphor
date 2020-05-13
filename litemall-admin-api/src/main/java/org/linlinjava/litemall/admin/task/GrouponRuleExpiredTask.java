package org.linlinjava.litemall.admin.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.task.Task;
import org.linlinjava.litemall.core.util.BeanUtil;
import org.linlinjava.litemall.db.common.constans.GrouponConstant;
import org.linlinjava.litemall.db.common.util.OrderUtil;
import org.linlinjava.litemall.db.entity.Groupon;
import org.linlinjava.litemall.db.entity.GrouponRules;
import org.linlinjava.litemall.db.entity.Order;
import org.linlinjava.litemall.db.service.IGrouponRulesService;
import org.linlinjava.litemall.db.service.IGrouponService;
import org.linlinjava.litemall.db.service.IOrderService;

import java.util.List;

public class GrouponRuleExpiredTask extends Task {
    private final Log logger = LogFactory.getLog(GrouponRuleExpiredTask.class);
    private int grouponRuleId = -1;

    public GrouponRuleExpiredTask(Integer grouponRuleId, long delayInMilliseconds){
        super("GrouponRuleExpiredTask-" + grouponRuleId, delayInMilliseconds);
        this.grouponRuleId = grouponRuleId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---团购规则过期---" + this.grouponRuleId);

        IOrderService orderService = BeanUtil.getBean(IOrderService.class);
        IGrouponService grouponService = BeanUtil.getBean(IGrouponService.class);
        IGrouponRulesService grouponRulesService = BeanUtil.getBean(IGrouponRulesService.class);

        GrouponRules grouponRules = grouponRulesService.getById(grouponRuleId);
        if(grouponRules == null){
            return;
        }
        if(!grouponRules.getStatus().equals(GrouponConstant.RULE_STATUS_ON)){
            return;
        }

        // 团购活动取消
        grouponRules.setStatus(GrouponConstant.RULE_STATUS_DOWN_EXPIRE);
        grouponRulesService.updateById(grouponRules);

        List<Groupon> grouponList = grouponService.list(new LambdaQueryWrapper<Groupon>()
                .eq(Groupon::getRulesId,grouponRuleId));
        // 用户团购处理
        for(Groupon groupon : grouponList){
            Integer status = groupon.getStatus();
            Order order = orderService.getById(groupon.getOrderId());
            if(status.equals(GrouponConstant.STATUS_NONE)){
                groupon.setStatus(GrouponConstant.STATUS_FAIL);
                grouponService.updateById(groupon);
            }
            else if(status.equals(GrouponConstant.STATUS_ON)){
                // 如果团购进行中
                // (1) 团购设置团购失败等待退款状态
                groupon.setStatus(GrouponConstant.STATUS_FAIL);
                grouponService.updateById(groupon);
                // (2) 团购订单申请退款
                if(OrderUtil.isPayStatus(order)) {
                    order.setOrderStatus(OrderUtil.STATUS_REFUND);
                    orderService.updateWithOptimisticLocker(order);
                }
            }
        }
        logger.info("系统结束处理延时任务---团购规则过期---" + this.grouponRuleId);
    }
}
