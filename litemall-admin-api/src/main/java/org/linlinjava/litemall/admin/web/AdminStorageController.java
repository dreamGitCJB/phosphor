package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Storage;
import org.linlinjava.litemall.db.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/admin/storage")
@Validated
public class AdminStorageController {
    private final Log logger = LogFactory.getLog(AdminStorageController.class);

    @Autowired
    private StorageService storageService;
    @Autowired
    private IStorageService litemallStorageService;

    @RequiresPermissions("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "查询")
    @GetMapping("/list")
    public Object list(String key, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        Page pageData = new Page(page, limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<Storage> storageList = litemallStorageService.page(pageData, new LambdaQueryWrapper<Storage>()
                .eq(!StringUtils.isEmpty(key), Storage::getKey, key)
                .like(!StringUtils.isEmpty(name), Storage::getName, name));
        return ResponseUtil.okPageList(storageList);
    }

    @RequiresPermissions("admin:storage:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "上传")
    @PostMapping("/create")
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        Storage storage = storageService.store(file.getInputStream(), file.getSize(),
                file.getContentType(), originalFilename);
        return ResponseUtil.ok(storage);
    }

    @RequiresPermissions("admin:storage:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "详情")
    @PostMapping("/read")
    public Object read(@NotNull Integer id) {
        Storage storageInfo = litemallStorageService.getById(id);
        if (storageInfo == null) {
            return ResponseUtil.badArgumentValue();
        }
        return ResponseUtil.ok(storageInfo);
    }

    @RequiresPermissions("admin:storage:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Storage litemallStorage) {
        if (!litemallStorageService.updateById(litemallStorage)) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(litemallStorage);
    }

    @RequiresPermissions("admin:storage:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Storage litemallStorage) {
        String key = litemallStorage.getKey();
        if (StringUtils.isEmpty(key)) {
            return ResponseUtil.badArgument();
        }
        litemallStorageService.remove(new LambdaQueryWrapper<Storage>().eq(Storage::getKey, key));
        storageService.delete(key);
        return ResponseUtil.ok();
    }
}
