package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Coupon;
import org.linlinjava.litemall.db.entity.CouponUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优惠券用户使用表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICouponUserService extends IService<CouponUser> {

    void assignForRegister(Integer userId);

    List<CouponUser> queryAll(Integer userId);

    IPage<CouponUser> queryList(Integer userId, Integer couponId, Integer status, Integer current, Integer size, String sort, String order);

    List<CouponUser> queryList(Integer userId, Integer couponId, Integer status, String sort, String order);

    Integer countUserAndCoupon(Integer userId,Integer couponId);
}
