package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Goods;
import org.linlinjava.litemall.db.entity.Topic;
import org.linlinjava.litemall.db.service.IGoodsService;
import org.linlinjava.litemall.db.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/topic")
@Validated
public class AdminTopicController {
    private final Log logger = LogFactory.getLog(AdminTopicController.class);

    @Autowired
    private ITopicService topicService;
    @Autowired
    private IGoodsService goodsService;

    @RequiresPermissions("admin:topic:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String title, String subtitle,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort(accepts = {"id", "add_time", "price"}) @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        Page pageData = new Page(page, limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<Topic> topicList = topicService.page(pageData, new LambdaQueryWrapper<Topic>()
                .like(!StringUtils.isEmpty(title), Topic::getTitle, title)
                .like(!StringUtils.isEmpty(subtitle), Topic::getSubtitle, subtitle));
        return ResponseUtil.okPageList(topicList);
    }

    private Object validate(Topic topic) {
        String title = topic.getTitle();
        if (StringUtils.isEmpty(title)) {
            return ResponseUtil.badArgument();
        }
        String content = topic.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }
        BigDecimal price = topic.getPrice();
        if (price == null) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:topic:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody Topic topic) {
        Object error = validate(topic);
        if (error != null) {
            return error;
        }
        topicService.save(topic);
        return ResponseUtil.ok(topic);
    }

    @RequiresPermissions("admin:topic:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        Topic topic = topicService.getById(id);
        Integer[] goodsIds = topic.getGoods();
        Collection<Goods> goodsList = null;
        if (goodsIds == null || goodsIds.length == 0) {
            goodsList = new ArrayList<>();
        } else {
            goodsList = goodsService.listByIds(Arrays.asList(goodsIds));
        }
        Map<String, Object> data = new HashMap<>(2);
        data.put("topic", topic);
        data.put("goodsList", goodsList);
        return ResponseUtil.ok(data);
    }

    @RequiresPermissions("admin:topic:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Topic topic) {
        Object error = validate(topic);
        if (error != null) {
            return error;
        }
        if (!topicService.updateById(topic)) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(topic);
    }

    @RequiresPermissions("admin:topic:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Topic topic) {
        topicService.removeById(topic.getId());
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:topic:batch-delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "批量删除")
    @PostMapping("/batch-delete")
    public Object batchDelete(@RequestBody String body) {
        List<Integer> ids = JacksonUtil.parseIntegerList(body, "ids");
        topicService.removeByIds(ids);
        return ResponseUtil.ok();
    }
}
