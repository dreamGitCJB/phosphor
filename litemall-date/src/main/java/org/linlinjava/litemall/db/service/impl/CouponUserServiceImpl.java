package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.constans.CouponConstant;
import org.linlinjava.litemall.db.common.constans.CouponUserConstant;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Coupon;
import org.linlinjava.litemall.db.entity.CouponUser;
import org.linlinjava.litemall.db.mapper.CouponUserMapper;
import org.linlinjava.litemall.db.service.ICouponService;
import org.linlinjava.litemall.db.service.ICouponUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 优惠券用户使用表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements ICouponUserService {

    @Autowired
    private ICouponService couponService;

    /**
     * 分发注册优惠券
     *
     * @param userId
     * @return
     */
    @Override
    public void assignForRegister(Integer userId) {
        List<Coupon> couponList = couponService.queryRegister();
        for(Coupon coupon : couponList){
            Integer couponId = coupon.getId();

            Integer count = this.count(new LambdaQueryWrapper<CouponUser>().eq(CouponUser::getUserId, userId)
                    .eq(CouponUser::getCouponId, couponId));
            if (count > 0) {
                continue;
            }

            Integer limit = coupon.getLimit();
            while(limit > 0){
                CouponUser couponUser = new CouponUser();
                couponUser.setCouponId(couponId);
                couponUser.setUserId(userId);
                Integer timeType = coupon.getTimeType();
                if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
                    couponUser.setStartTime(coupon.getStartTime());
                    couponUser.setEndTime(coupon.getEndTime());
                }
                else{
                    LocalDateTime now = LocalDateTime.now();
                    couponUser.setStartTime(now);
                    couponUser.setEndTime(now.plusDays(coupon.getDays()));
                }
                this.save(couponUser);

                limit--;
            }
        }

    }

    @Override
    public List<CouponUser> queryAll(Integer userId) {
        return queryList(userId, null, CouponUserConstant.STATUS_USABLE, "add_time", "desc");
    }

    @Override
    public IPage<CouponUser> queryList(Integer userId, Integer couponId, Integer status, Integer current, Integer size, String sort, String order) {

        Page page = new Page(current, size);
        PageUtil.pagetoPage(page,sort,order);

        IPage<CouponUser> couponUserIPage = this.page(page, new LambdaQueryWrapper<CouponUser>().eq(userId != null, CouponUser::getUserId, userId)
                .eq(couponId != null, CouponUser::getCouponId, couponId)
                .eq(status != null, CouponUser::getStatus, status));
        return couponUserIPage;
    }

    @Override
    public List<CouponUser> queryList(Integer userId, Integer couponId, Integer status, String sort, String order) {

        Page page = new Page(1, Integer.MAX_VALUE);
        PageUtil.pagetoPage(page,sort,order);

        IPage<CouponUser> couponUserIPage = this.page(page, new LambdaQueryWrapper<CouponUser>().eq(userId != null, CouponUser::getUserId, userId)
                .eq(couponId != null, CouponUser::getCouponId, couponId)
                .eq(status != null, CouponUser::getStatus, status));
        return couponUserIPage.getRecords();
    }

    @Override
    public Integer countUserAndCoupon(Integer userId, Integer couponId) {
        return this.count(new LambdaQueryWrapper<CouponUser>().eq(CouponUser::getUserId, userId).eq(CouponUser::getCouponId, couponId));
    }
}
