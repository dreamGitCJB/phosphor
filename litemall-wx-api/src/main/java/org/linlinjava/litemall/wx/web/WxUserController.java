package org.linlinjava.litemall.wx.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.common.constans.CommonConstant;
import org.linlinjava.litemall.db.common.enums.IntegralType;
import org.linlinjava.litemall.db.common.result.ResponseUtil;
import org.linlinjava.litemall.db.common.enums.RandomType;
import org.linlinjava.litemall.db.common.util.Func;
import org.linlinjava.litemall.db.common.util.StringUtil;
import org.linlinjava.litemall.db.entity.IntegralRecord;
import org.linlinjava.litemall.db.entity.User;
import org.linlinjava.litemall.db.service.IIntegralRecordService;
import org.linlinjava.litemall.db.service.IOrderService;
import org.linlinjava.litemall.db.service.IUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.dto.InviteRecordsDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private IIntegralRecordService integralRecordService;

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

	/**
	 * 填写手机号
	 * @param userId
	 * @param map
	 * @return
	 */
	@PostMapping("/invited-phone")
	public Object invitedPhone(@LoginUser Integer userId,@RequestBody Map<String, String> map) {

		String phone = map.get("invitePhone");

		Integer inviteUserId = Func.toInt(map.get("inviteUserId"));

		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		User user = userService.getOne(Wrappers.lambdaQuery(User.class).eq(User::getInviteCode, map.get("invitePhone")));
		if (user != null) {
			return ResponseUtil.fail(1000,"抱歉！您已经接受过邀请了");
		}

		IntegralRecord one = integralRecordService.getOne(Wrappers.lambdaQuery(IntegralRecord.class).eq(IntegralRecord::getValueId, userId).eq(IntegralRecord::getIntegralType, IntegralType.INVITE_NEW_MEMBERS.getCode()));
		if (one != null) {
			return ResponseUtil.fail(1000,"抱歉！您已经接受过邀请了");
		}

		/**
		 * 更新手机号
		 */
		User updateUser = new User();
		updateUser.setId(userId);
		updateUser.setMobile(phone);
		boolean b = userService.updateById(updateUser);

		//保存积分记录
		IntegralRecord integralRecord = IntegralRecord.builder()
				.userId(inviteUserId)
				.integralType(IntegralType.INVITE_NEW_MEMBERS)
				.integral(new BigDecimal("25"))
				.valueId(userId)
				.build();
		integralRecordService.save(integralRecord);


		return ResponseUtil.status(b);
	}

	/**
	 * 填写邀请码
	 * @param userId
	 * @param map
	 * @return
	 */
    @PostMapping("/invited-code")
	public Object invitedCode(@LoginUser Integer userId,@RequestBody Map<String, String> map) {
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		User user = userService.getOne(Wrappers.lambdaQuery(User.class).eq(User::getInviteCode, map.get("inviteCode")));
		if (user == null) {
			return ResponseUtil.fail(1000,"邀请码不存在");
		}
		User updateUser = new User();
		updateUser.setId(userId);
		updateUser.setInvitedId(user.getId());
		boolean b = userService.updateById(updateUser);

		//保存积分记录
		IntegralRecord integralRecord = IntegralRecord.builder()
				.userId(user.getId())
				.integralType(IntegralType.INVITE_NEW_MEMBERS)
				.integral(new BigDecimal("25"))
				.valueId(userId)
				.build();
		integralRecordService.save(integralRecord);


		return ResponseUtil.status(b);
	}

	/**
	 * 获取我的邀请码和邀请记录
	 * @param userId
	 * @return
	 */
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

		List<IntegralRecord> integralRecord = integralRecordService.list(Wrappers.lambdaQuery(IntegralRecord.class).eq(IntegralRecord::getUserId, userId).eq(IntegralRecord::getIntegralType,IntegralType.INVITE_NEW_MEMBERS.getCode()).orderByDesc(IntegralRecord::getAddTime));

		Map<Integer, IntegralRecord> collect = integralRecord.stream().collect(Collectors.toMap(IntegralRecord::getValueId, obj -> obj));

		List<InviteRecordsDTO> list = null;
		if (collect.keySet().size() > 0) {
			list = userService.listByIds(collect.keySet()).stream().filter(obj -> collect.keySet().contains(obj.getId())).map(obj -> {
				InviteRecordsDTO inviteRecordsDTO = new InviteRecordsDTO();
				inviteRecordsDTO.setId(obj.getId());
				inviteRecordsDTO.setAvatar(obj.getAvatar());
				inviteRecordsDTO.setNickname(obj.getNickname());
				IntegralRecord record = collect.get(obj.getId());
				inviteRecordsDTO.setInvitedTime(record.getAddTime());
				inviteRecordsDTO.setIntegral(record.getIntegral());
				return inviteRecordsDTO;
			}).collect(Collectors.toList());
		}

		Map<String, Object> map = new HashMap<>(2);
		map.put("inviteCode", random);
		map.put("inviteList", Optional.ofNullable(list).orElse(new ArrayList<>()));
		return ResponseUtil.ok(map);
	}

	/**
	 *
	 * @param userId
	 * @param limit
	 * @param size
	 * @param showType 0明细 1收入 2 支出
	 * @return
	 */
	@GetMapping("/integral-record")
	public Object integralRecord(@LoginUser Integer userId,
								 @RequestParam(defaultValue = CommonConstant.DEFAULT_PAGE) int limit,
								 @RequestParam(defaultValue = CommonConstant.DEFAULT_SIZE) int size,
								 @RequestParam(defaultValue = CommonConstant.DEFAULT_ZERO) int showType) {

		Page page = new Page(limit, size);
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(OrderItem.desc("add_time"));
		page.setOrders(orderItems);

    	IPage<IntegralRecord> iPage = integralRecordService.page(page,Wrappers.lambdaQuery(IntegralRecord.class)
				.eq(IntegralRecord::getUserId, userId)
				.gt(showType == 1,IntegralRecord::getIntegral, 0)
				.lt(showType == 2, IntegralRecord::getIntegral, 0));
		return ResponseUtil.okPageList(iPage);
	}

	@GetMapping("/integral-total")
	public Object integralTotal(@LoginUser Integer userId) {
		if (userId == null) {
			return ResponseUtil.unlogin();
		}

		BigDecimal integral = integralRecordService.list(Wrappers.lambdaQuery(IntegralRecord.class).eq(IntegralRecord::getUserId, userId)).stream().map(IntegralRecord::getIntegral).reduce(BigDecimal.ZERO, BigDecimal::add);

		Map<String,Object> map = new HashMap<>(1);
		map.put("integral",integral);

		return ResponseUtil.ok(map);
	}
}
