package org.linlinjava.litemall.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_order")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 售后状态，0是可申请，1是用户已申请，2是管理员审核通过，3是管理员退款成功，4是管理员审核拒绝，5是用户已取消
     */
    private Integer aftersaleStatus;

    /**
     * 收货人名称
     */
    private String consignee;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 收货具体地址
     */
    private String address;

    /**
     * 用户订单留言
     */
    private String message;

    /**
     * 商品总费用
     */
    private BigDecimal goodsPrice;

    /**
     * 配送费用
     */
    private BigDecimal freightPrice;

    /**
     * 优惠券减免
     */
    private BigDecimal couponPrice;

    /**
     * 用户积分减免
     */
    private BigDecimal integralPrice;

    /**
     * 团购优惠价减免
     */
    private BigDecimal grouponPrice;

    /**
     * 订单费用， = goods_price + freight_price - coupon_price
     */
    private BigDecimal orderPrice;

    /**
     * 实付费用， = order_price - integral_price
     */
    private BigDecimal actualPrice;

    /**
     * 微信付款编号
     */
    private String payId;

    /**
     * 微信付款时间
     */
    private LocalDateTime payTime;

    /**
     * 发货编号
     */
    private String shipSn;

    /**
     * 发货快递公司
     */
    private String shipChannel;

    /**
     * 发货开始时间
     */
    private LocalDateTime shipTime;

    /**
     * 实际退款金额，（有可能退款金额小于实际支付金额）
     */
    private BigDecimal refundAmount;

    /**
     * 退款方式
     */
    private String refundType;

    /**
     * 退款备注
     */
    private String refundContent;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 用户确认收货时间
     */
    private LocalDateTime confirmTime;

    /**
     * 待评价订单商品数量
     */
    private Integer comments;

    /**
     * 订单关闭时间
     */
    private LocalDateTime endTime;




}
