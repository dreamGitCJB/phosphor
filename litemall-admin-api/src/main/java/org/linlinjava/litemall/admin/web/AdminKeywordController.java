package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Keyword;
import org.linlinjava.litemall.db.service.IKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/keyword")
@Validated
public class AdminKeywordController {
    private final Log logger = LogFactory.getLog(AdminKeywordController.class);

    @Autowired
    private IKeywordService keywordService;

    @RequiresPermissions("admin:keyword:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "查询")
    @GetMapping("/list")
    public Object list(String keyword, String url,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        Page pageData = new Page(page,limit);
        PageUtil.pagetoPage(pageData,sort,order);
        IPage<Keyword> keywordList = keywordService.page(pageData, new LambdaQueryWrapper<Keyword>()
                .like(!StringUtils.isEmpty(keyword),Keyword::getKeyword, keyword)
                .like(!StringUtils.isEmpty(url), Keyword::getUrl, url));
        return ResponseUtil.okPageList(keywordList);
    }

    private Object validate(Keyword keywords) {
        String keyword = keywords.getKeyword();
        if (StringUtils.isEmpty(keyword)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:keyword:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody Keyword keyword) {
        Object error = validate(keyword);
        if (error != null) {
            return error;
        }
        keywordService.save(keyword);
        return ResponseUtil.ok(keyword);
    }

    @RequiresPermissions("admin:keyword:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        Keyword keyword = keywordService.getById(id);
        return ResponseUtil.ok(keyword);
    }

    @RequiresPermissions("admin:keyword:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Keyword keyword) {
        Object error = validate(keyword);
        if (error != null) {
            return error;
        }
        if (!keywordService.updateById(keyword)) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(keyword);
    }

    @RequiresPermissions("admin:keyword:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "关键词"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Keyword keyword) {
        Integer id = keyword.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        keywordService.removeById(id);
        return ResponseUtil.ok();
    }

}
