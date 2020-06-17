package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.task.GrouponRuleExpiredTask;
import org.linlinjava.litemall.admin.util.AdminResponseCode;
import org.linlinjava.litemall.core.task.TaskService;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.constans.GrouponConstant;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.entity.Groupon;
import org.linlinjava.litemall.db.entity.GrouponRules;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.linlinjava.litemall.db.service.IGrouponRulesService;
import org.linlinjava.litemall.db.service.IGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/groupon")
@Validated
public class AdminGrouponController {
    private final Log logger = LogFactory.getLog(AdminGrouponController.class);

    @Autowired
    private IGrouponRulesService rulesService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IGrouponService grouponService;
    @Autowired
    private TaskService taskService;

    @RequiresPermissions("admin:groupon:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "详情")
    @GetMapping("/listRecord")
    public Object listRecord(String grouponRuleId,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @Sort @RequestParam(defaultValue = "add_time") String sort,
                             @Order @RequestParam(defaultValue = "desc") String order) {


        Page pageData = new Page(page, limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<Groupon> grouponList = grouponService.page(pageData, new LambdaQueryWrapper<Groupon>().eq(Groupon::getGrouponId, 0)
                .eq(!StringUtils.isEmpty(grouponRuleId), Groupon::getRulesId, grouponRuleId)
                .ne(Groupon::getStatus, GrouponConstant.STATUS_NONE));


        List<Map<String, Object>> groupons = new ArrayList<>();
        for (Groupon groupon : grouponList.getRecords()) {
            try {
                Map<String, Object> recordData = new HashMap<>();
                List<Groupon> subGrouponList = grouponService.queryJoinRecord(groupon.getId());
                GrouponRules rules = rulesService.getById(groupon.getRulesId());
                Goods goods = goodsService.getById(rules.getGoodsId());

                recordData.put("groupon", groupon);
                recordData.put("subGroupons", subGrouponList);
                recordData.put("rules", rules);
                recordData.put("goods", goods);

                groupons.add(recordData);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        IPage iPage = new PageUtil<Map<String, Object>>().pagetoPage(grouponList, groupons);

        return ResponseUtil.okPageList(iPage);
    }

    @RequiresPermissions("admin:groupon:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String goodsId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {



        Page pageData = new Page(page, limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<GrouponRules> rulesList = rulesService.page(pageData, new LambdaQueryWrapper<GrouponRules>().eq(!StringUtils.isEmpty(goodsId),GrouponRules::getGoodsId, goodsId));

        return ResponseUtil.okPageList(rulesList);
    }

    private Object validate(GrouponRules grouponRules) {
        Integer goodsId = grouponRules.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }
        BigDecimal discount = grouponRules.getDiscount();
        if (discount == null) {
            return ResponseUtil.badArgument();
        }
        Integer discountMember = grouponRules.getDiscountMember();
        if (discountMember == null) {
            return ResponseUtil.badArgument();
        }
        LocalDateTime expireTime = grouponRules.getExpireTime();
        if (expireTime == null) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:groupon:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody GrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        GrouponRules rules = rulesService.getById(grouponRules.getId());
        if(rules == null){
            return ResponseUtil.badArgumentValue();
        }
        if(!rules.getStatus().equals(GrouponConstant.RULE_STATUS_ON)){
            return ResponseUtil.fail(AdminResponseCode.GROUPON_GOODS_OFFLINE, "团购已经下线");
        }

        Integer goodsId = grouponRules.getGoodsId();
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());

        if (!rulesService.updateById(grouponRules)) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:groupon:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody GrouponRules grouponRules) {
        Object error = validate(grouponRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = grouponRules.getGoodsId();
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return ResponseUtil.fail(AdminResponseCode.GROUPON_GOODS_UNKNOWN, "团购商品不存在");
        }

        int count = rulesService.count(new LambdaQueryWrapper<GrouponRules>().eq(GrouponRules::getGoodsId, goodsId)
                .eq(GrouponRules::getStatus, GrouponConstant.RULE_STATUS_ON));

        if(count > 0){
            return ResponseUtil.fail(AdminResponseCode.GROUPON_GOODS_EXISTED, "团购商品已经存在");
        }

        grouponRules.setGoodsName(goods.getName());
        grouponRules.setPicUrl(goods.getPicUrl());
        grouponRules.setStatus(GrouponConstant.RULE_STATUS_ON);
        grouponRules.setAddTime(LocalDateTime.now());
        grouponRules.setUpdateTime(LocalDateTime.now());
        rulesService.save(grouponRules);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = grouponRules.getExpireTime();
        long delay = ChronoUnit.MILLIS.between(now, expire);
        // 团购过期任务
        taskService.addTask(new GrouponRuleExpiredTask(grouponRules.getId(), delay));

        return ResponseUtil.ok(grouponRules);
    }

    @RequiresPermissions("admin:groupon:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "团购管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody GrouponRules grouponRules) {
        Integer id = grouponRules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        rulesService.removeById(id);
        return ResponseUtil.ok();
    }
}
