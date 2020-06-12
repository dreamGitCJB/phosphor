package org.linlinjava.litemall.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.core.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Admin;
import org.linlinjava.litemall.db.entity.Notice;
import org.linlinjava.litemall.db.entity.NoticeAdmin;
import org.linlinjava.litemall.db.service.IAdminService;
import org.linlinjava.litemall.db.service.INoticeAdminService;
import org.linlinjava.litemall.db.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.linlinjava.litemall.admin.util.AdminResponseCode.ADMIN_INVALID_ACCOUNT;

@RestController
@RequestMapping("/admin/profile")
@Validated
public class AdminProfileController {
    private final Log logger = LogFactory.getLog(AdminProfileController.class);

    @Autowired
    private IAdminService adminService;
    @Autowired
    private INoticeService noticeService;
    @Autowired
    private INoticeAdminService noticeAdminService;

    @RequiresAuthentication
    @PostMapping("/password")
    public Object create(@RequestBody String body) {
        String oldPassword = JacksonUtil.parseString(body, "oldPassword");
        String newPassword = JacksonUtil.parseString(body, "newPassword");
        if (StringUtils.isEmpty(oldPassword)) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseUtil.badArgument();
        }

        Subject currentUser = SecurityUtils.getSubject();
        Admin admin = (Admin) currentUser.getPrincipal();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, admin.getPassword())) {
            return ResponseUtil.fail(ADMIN_INVALID_ACCOUNT, "账号密码不对");
        }

        String encodedNewPassword = encoder.encode(newPassword);
        admin.setPassword(encodedNewPassword);

        adminService.updateById(admin);
        return ResponseUtil.ok();
    }

    private Integer getAdminId(){
        Subject currentUser = SecurityUtils.getSubject();
        Admin admin = (Admin) currentUser.getPrincipal();
        return admin.getId();
    }

    @RequiresAuthentication
    @GetMapping("/nnotice")
    public Object nNotice() {
        int count = noticeAdminService.count(new LambdaQueryWrapper<NoticeAdmin>().eq(NoticeAdmin::getAdminId, getAdminId()).isNull(NoticeAdmin::getReadTime));
        return ResponseUtil.ok(count);
    }

    @RequiresAuthentication
    @GetMapping("/lsnotice")
    public Object lsNotice(String title, String type,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer limit,
                            @Sort @RequestParam(defaultValue = "add_time") String sort,
                            @Order @RequestParam(defaultValue = "desc") String order) {

        Page pageData = new Page(page, limit);
        PageUtil.pagetoPage(pageData, sort, order);

        IPage<NoticeAdmin> noticeList = noticeAdminService.page(pageData,new LambdaQueryWrapper<NoticeAdmin>()
                .isNotNull(type.equals("read"), NoticeAdmin::getReadTime)
                .isNull(type.equals("unread"), NoticeAdmin::getReadTime)
                .eq(NoticeAdmin::getAdminId,getAdminId() ));
        return ResponseUtil.okPageList(noticeList);
    }

    @RequiresAuthentication
    @PostMapping("/catnotice")
    public Object catNotice(@RequestBody String body) {
        Integer noticeId = JacksonUtil.parseInteger(body, "noticeId");
        if(noticeId == null){
            return ResponseUtil.badArgument();
        }

        NoticeAdmin noticeAdmin = noticeAdminService.getOne(new LambdaQueryWrapper<NoticeAdmin>()
                .eq(NoticeAdmin::getNoticeId,noticeId)
                .eq(NoticeAdmin::getAdminId, getAdminId()));
        if(noticeAdmin == null){
           return ResponseUtil.badArgumentValue();
        }
        // 更新通知记录中的时间
        noticeAdmin.setReadTime(LocalDateTime.now());
        noticeAdminService.updateById(noticeAdmin);

        // 返回通知的相关信息
        Map<String, Object> data = new HashMap<>();
        Notice notice = noticeService.getById(noticeId);
        data.put("title", notice.getTitle());
        data.put("content", notice.getContent());
        data.put("time", notice.getUpdateTime());
        Integer adminId = notice.getAdminId();
        if(adminId.equals(0)){
            data.put("admin", "系统");
        }
        else{
            Admin admin = adminService.getById(notice.getAdminId());
            data.put("admin", admin.getUsername());
            data.put("avatar", admin.getAvatar());
        }
        return ResponseUtil.ok(data);
    }

    @RequiresAuthentication
    @PostMapping("/bcatnotice")
    public Object bcatNotice(@RequestBody String body) {
        List<Integer> ids = JacksonUtil.parseIntegerList(body, "ids");

        NoticeAdmin noticeAdmin = new NoticeAdmin();
        noticeAdmin.setReadTime(LocalDateTime.now());

        noticeAdminService.update(noticeAdmin, new LambdaQueryWrapper<NoticeAdmin>()
                .in(NoticeAdmin::getId, ids).eq(NoticeAdmin::getAdminId,getAdminId()));
        return ResponseUtil.ok();
    }

    @RequiresAuthentication
    @PostMapping("/rmnotice")
    public Object rmNotice(@RequestBody String body) {
        Integer id = JacksonUtil.parseInteger(body, "id");
        if(id == null){
            return ResponseUtil.badArgument();
        }

        noticeAdminService.remove(new LambdaQueryWrapper<NoticeAdmin>().eq(NoticeAdmin::getAdminId,getAdminId()).eq(NoticeAdmin::getId, id));
        return ResponseUtil.ok();
    }

    @RequiresAuthentication
    @PostMapping("/brmnotice")
    public Object brmNotice(@RequestBody String body) {
        List<Integer> ids = JacksonUtil.parseIntegerList(body, "ids");

        noticeAdminService.remove(new LambdaQueryWrapper<NoticeAdmin>().eq(NoticeAdmin::getAdminId, getAdminId()).in(NoticeAdmin::getNoticeId,ids));
        return ResponseUtil.ok();
    }

}
