package org.linlinjava.litemall.wx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.entity.GrouponRules;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.linlinjava.litemall.db.service.IGrouponRulesService;
import org.linlinjava.litemall.db.service.IGrouponService;
import org.linlinjava.litemall.wx.vo.GrouponRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WxGrouponRuleService {
    private final Log logger = LogFactory.getLog(WxGrouponRuleService.class);

    @Autowired
    private IGrouponRulesService grouponRulesService;
    @Autowired
    private IGrouponService grouponService;
    @Autowired
    private IGoodsService goodsService;


    public IPage<GrouponRuleVo> queryList(Integer page, Integer size) {
        return queryList(page, size, "add_time", "desc");
    }


    public IPage<GrouponRuleVo> queryList(Integer current, Integer size, String sort, String order) {
        IPage<GrouponRuleVo> grouponRuleVoPage;

        Page page = new Page(current, size);

        PageUtil.pagetoPage(page,sort, order);
        IPage<GrouponRules> grouponPage = grouponRulesService.page(page);
        List<Integer> goodsIds = grouponPage.getRecords().stream().map(GrouponRules::getGoodsId).collect(Collectors.toList());
        if (goodsIds.size() > 0) {
            Map<Integer, Goods> goodsMap = goodsService.listByIds(goodsIds).stream().collect(Collectors.toMap(Goods::getId, o -> o));


            List<GrouponRuleVo> grouponRuleVos = grouponPage.getRecords().stream().filter(o -> goodsMap.containsKey(o.getGoodsId())).map(obj -> {
                Integer goodsId = obj.getGoodsId();
                Goods goods = goodsMap.get(goodsId);

                GrouponRuleVo grouponRuleVo = new GrouponRuleVo();
                grouponRuleVo.setId(goods.getId());
                grouponRuleVo.setName(goods.getName());
                grouponRuleVo.setBrief(goods.getBrief());
                grouponRuleVo.setPicUrl(goods.getPicUrl());
                grouponRuleVo.setCounterPrice(goods.getCounterPrice());
                grouponRuleVo.setRetailPrice(goods.getRetailPrice());
                grouponRuleVo.setGrouponPrice(goods.getRetailPrice().subtract(obj.getDiscount()));
                grouponRuleVo.setGrouponDiscount(obj.getDiscount());
                grouponRuleVo.setGrouponMember(obj.getDiscountMember());
                grouponRuleVo.setExpireTime(obj.getExpireTime());
                return grouponRuleVo;
            }).collect(Collectors.toList());


            grouponRuleVoPage = new Page<GrouponRuleVo>()
                    .setCurrent(grouponPage.getCurrent())
                    .setSize(grouponPage.getSize())
                    .setTotal(page.getTotal())
                    .setPages(page.getPages())
                    .setRecords(grouponRuleVos);

        } else {
            grouponRuleVoPage = new Page<GrouponRuleVo>()
                    .setCurrent(grouponPage.getCurrent())
                    .setSize(grouponPage.getSize())
                    .setTotal(0L)
                    .setPages(0L)
                    .setRecords(null);
        }

        return grouponRuleVoPage;

    }
}