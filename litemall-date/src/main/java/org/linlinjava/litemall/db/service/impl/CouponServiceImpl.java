package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.linlinjava.litemall.db.common.constans.CouponConstant;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Coupon;
import org.linlinjava.litemall.db.entity.CouponUser;
import org.linlinjava.litemall.db.entity.Groupon;
import org.linlinjava.litemall.db.mapper.CouponMapper;
import org.linlinjava.litemall.db.mapper.CouponUserMapper;
import org.linlinjava.litemall.db.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linlinjava.litemall.db.service.ICouponUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券信息及规则表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
@AllArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    private CouponUserMapper couponUserMapper;
    /**
     * 检测优惠券是否适合
     *
     * @param userId
     * @param couponId
     * @param checkedGoodsPrice
     * @return
     */
    @Override
    public Coupon checkCoupon(Integer userId, Integer couponId, Integer userCouponId, BigDecimal checkedGoodsPrice) {
        Coupon coupon = this.getById(couponId);
        if (coupon == null) {
            return null;
        }

        CouponUser couponUser = couponUserMapper.selectById(userCouponId);
        if (couponUser == null) {
            couponUser = couponUserMapper.selectOne(new LambdaQueryWrapper<CouponUser>().eq(CouponUser::getUserId,userId).eq(CouponUser::getCouponId, couponId));
        } else if (!couponId.equals(couponUser.getCouponId())) {
            return null;
        }

        if (couponUser == null) {
            return null;
        }

        // 检查是否超期
        Integer timeType = coupon.getTimeType();
        Integer days = coupon.getDays();
        LocalDateTime now = LocalDateTime.now();
        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
            if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
                return null;
            }
        }
        else if(timeType.equals(CouponConstant.TIME_TYPE_DAYS)) {
            LocalDateTime expired = couponUser.getAddTime().plusDays(days);
            if (now.isAfter(expired)) {
                return null;
            }
        }
        else {
            return null;
        }

        // 检测商品是否符合
        // TODO 目前仅支持全平台商品，所以不需要检测
        Integer goodType = coupon.getGoodsType();
        if (!goodType.equals(CouponConstant.GOODS_TYPE_ALL)) {
            return null;
        }

        // 检测订单状态
        Integer status = coupon.getStatus();
        if (!status.equals(CouponConstant.STATUS_NORMAL)) {
            return null;
        }
        // 检测是否满足最低消费
        if (checkedGoodsPrice.compareTo(coupon.getMin()) == -1) {
            return null;
        }

        return coupon;
    }

    @Override
    public List<Coupon> queryRegister() {
        List<Coupon> coupons = this.list(new LambdaQueryWrapper<Coupon>().eq(Coupon::getType, CouponConstant.TYPE_REGISTER).eq(Coupon::getStatus, CouponConstant.STATUS_NORMAL));
        return coupons;
    }

    @Override
    public IPage<Coupon> queryList(int current, int limit, String sort, String order) {

        Page page = new Page(current, limit);
        PageUtil.pagetoPage(page,sort,order);
        return this.page(page, new LambdaQueryWrapper<Coupon>().eq(Coupon::getType,CouponConstant.TYPE_COMMON).eq(Coupon::getStatus, CouponConstant.STATUS_NORMAL));
    }

    @Override
    public List<Coupon> queryAvailableList(Integer userId, int offset, int limit) {
        assert userId != null;
        // 过滤掉登录账号已经领取过的coupon
        List<Integer> collect = couponUserMapper.selectList(new LambdaQueryWrapper<CouponUser>().eq(CouponUser::getUserId, userId))
                .stream().map(CouponUser::getCouponId).collect(Collectors.toList());

        List<Coupon> coupons = this.list(new LambdaQueryWrapper<Coupon>().notIn(Coupon::getId, collect)
                .eq(Coupon::getType, CouponConstant.TYPE_COMMON)
                .eq(Coupon::getStatus, CouponConstant.STATUS_NORMAL)).stream().limit(limit).collect(Collectors.toList());
        return coupons;

    }
}
