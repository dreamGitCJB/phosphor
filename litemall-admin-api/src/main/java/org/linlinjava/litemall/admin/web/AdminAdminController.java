package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.service.LogHelper;
import org.linlinjava.litemall.core.util.RegexUtil;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Admin;
import org.linlinjava.litemall.db.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.linlinjava.litemall.admin.util.AdminResponseCode.*;

@RestController
@RequestMapping("/admin/admin")
@Validated
public class AdminAdminController {
    private final Log logger = LogFactory.getLog(AdminAdminController.class);

    @Autowired
    private IAdminService adminService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:admin:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        Page pageData = new Page(page,limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<Admin> adminList = adminService.page(pageData, new LambdaQueryWrapper<Admin>().like(!StringUtils.isEmpty(username), Admin::getUsername, username));
        return ResponseUtil.okPageList(adminList);
    }

    private Object validate(Admin admin) {
        String name = admin.getUsername();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isUsername(name)) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "管理员名称不符合规定");
        }
        String password = admin.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return ResponseUtil.fail(ADMIN_INVALID_PASSWORD, "管理员密码长度不能小于6");
        }
        return null;
    }

    @RequiresPermissions("admin:admin:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody Admin admin) {
        Object error = validate(admin);
        if (error != null) {
            return error;
        }

        String username = admin.getUsername();
        List<Admin> adminList = adminService.list(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername,username));
        if (adminList.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "管理员已经存在");
        }

        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        adminService.save(admin);
        logHelper.logAuthSucceed("添加管理员", username);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:admin:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        Admin admin = adminService.getById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:admin:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Admin admin) {
        Object error = validate(admin);
        if (error != null) {
            return error;
        }

        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        // 不允许管理员通过编辑接口修改密码
        admin.setPassword(null);

        if (!adminService.updateById(admin)) {
            return ResponseUtil.updatedDataFailed();
        }

        logHelper.logAuthSucceed("编辑管理员", admin.getUsername());
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:admin:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Admin admin) {
        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        // 管理员不能删除自身账号
        Subject currentUser = SecurityUtils.getSubject();
        Admin currentAdmin = (Admin) currentUser.getPrincipal();
        if (currentAdmin.getId().equals(anotherAdminId)) {
            return ResponseUtil.fail(ADMIN_DELETE_NOT_ALLOWED, "管理员不能删除自己账号");
        }

        adminService.removeById(anotherAdminId);
        logHelper.logAuthSucceed("删除管理员", admin.getUsername());
        return ResponseUtil.ok();
    }
}
