package org.linlinjava.litemall.wx.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.models.auth.In;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.common.enums.RandomType;
import org.linlinjava.litemall.db.common.util.Func;
import org.linlinjava.litemall.db.common.util.StringUtil;
import org.linlinjava.litemall.db.entity.User;
import org.linlinjava.litemall.db.service.IOrderService;
import org.linlinjava.litemall.db.service.IUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {
    private final Log logger = LogFactory.getLog(WxUserController.class);

    @Autowired
    private IOrderService orderService;

    @Autowired
	private IUserService userService;

    /**
     * 用户个人页面数据
     * <p>
     * 目前是用户订单统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("/index")
    public Object list(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Map<Object, Object> data = new HashMap<Object, Object>(1);
        data.put("order", orderService.orderInfo(userId));
        return ResponseUtil.ok(data);
    }

    @GetMapping("/invited")
	public Object listInvited(@LoginUser Integer userId) {
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		User user = userService.getById(userId);
		String random = user.getInviteCode();
		if (StringUtil.isBlank(user.getInviteCode())) {
			while (true) {
				random = Func.random(6, RandomType.ALL).toUpperCase();
				User one = userService.getOne(Wrappers.lambdaQuery(User.class).eq(User::getInviteCode, random));
				if (one == null) {
					break;
				}
			}
			User updateUser = new User();
			updateUser.setId(userId);
			updateUser.setInviteCode(random);
			userService.updateById(updateUser);
		}
		List<User> list = userService.list(Wrappers.lambdaQuery(User.class).eq(User::getInvitedId, userId));
		Map<String, Object> map = new HashMap<>(2);
		map.put("inviteCode", random);
		map.put("inviteList", list);
		return ResponseUtil.ok(map);
	}
}
