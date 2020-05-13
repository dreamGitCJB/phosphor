package org.linlinjava.litemall.admin.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.common.constans.CouponConstant;
import org.linlinjava.litemall.db.common.constans.CouponUserConstant;
import org.linlinjava.litemall.db.entity.Coupon;
import org.linlinjava.litemall.db.entity.CouponUser;
import org.linlinjava.litemall.db.service.ICouponService;
import org.linlinjava.litemall.db.service.ICouponUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测优惠券过期情况
 */
@Component
public class CouponJob {
    private final Log logger = LogFactory.getLog(CouponJob.class);

    @Autowired
    private ICouponService couponService;
    @Autowired
    private ICouponUserService couponUserService;

    /**
     * 每隔一个小时检查
     * TODO
     * 注意，因为是相隔一个小时检查，因此导致优惠券真正超时时间可能比设定时间延迟1个小时
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void checkCouponExpired() {
        logger.info("系统开启任务检查优惠券是否已经过期");


        List<Coupon> couponList = couponService.list(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getStatus, CouponConstant.STATUS_NORMAL)
                .eq(Coupon::getTimeType, CouponConstant.TIME_TYPE_TIME)
                .lt(Coupon::getEndTime, LocalDateTime.now()));
        for (Coupon coupon : couponList) {
            coupon.setStatus(CouponConstant.STATUS_EXPIRED);
            couponService.updateById(coupon);
        }

        List<CouponUser> couponUserList = couponUserService.list(new LambdaQueryWrapper<CouponUser>()
                .eq(CouponUser::getStatus, CouponUserConstant.STATUS_USABLE)
                .lt(CouponUser::getEndTime, LocalDateTime.now()));
        for (CouponUser couponUser : couponUserList) {
            couponUser.setStatus(CouponUserConstant.STATUS_EXPIRED);
            couponUserService.updateById(couponUser);
        }

        logger.info("系统结束任务检查优惠券是否已经过期");
    }

}
