package org.linlinjava.litemall.db.entity;

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
 * 团购活动表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_groupon")
public class Groupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的订单ID
     */
    private Integer orderId;

    /**
     * 如果是开团用户，则groupon_id是0；如果是参团用户，则groupon_id是团购活动ID
     */
    private Integer grouponId;

    /**
     * 团购规则ID，关联litemall_groupon_rules表ID字段
     */
    private Integer rulesId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 团购分享图片地址
     */
    private String shareUrl;

    /**
     * 开团用户ID
     */
    private Integer creatorUserId;

    /**
     * 开团时间
     */
    private LocalDateTime creatorUserTime;

    /**
     * 团购活动状态，开团未支付则0，开团中则1，开团失败则2
     */
    private Integer status;




}
