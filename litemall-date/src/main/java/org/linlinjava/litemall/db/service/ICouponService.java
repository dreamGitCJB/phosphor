package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 优惠券信息及规则表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICouponService extends IService<Coupon> {

    /**
     * 检测优惠券是否适合
     *
     * @param userId
     * @param couponId
     * @param checkedGoodsPrice
     * @return
     */
    Coupon checkCoupon(Integer userId, Integer couponId, Integer userCouponId, BigDecimal checkedGoodsPrice);

    List<Coupon> queryRegister();


    IPage<Coupon> queryList(int current, int limit, String sort, String order);


    List<Coupon> queryAvailableList(Integer userId, int offset, int limit);



}
