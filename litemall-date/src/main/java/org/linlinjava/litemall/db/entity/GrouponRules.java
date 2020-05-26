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
 * 团购规则表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_groupon_rules")
public class GrouponRules extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 商品表的商品ID
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片或者商品货品图片
     */
    private String picUrl;

    /**
     * 优惠金额
     */
    private BigDecimal discount;

    /**
     * 达到优惠条件的人数
     */
    private Integer discountMember;

    /**
     * 团购过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 团购规则状态，正常上线则0，到期自动下线则1，管理手动下线则2
     */
    private Integer status;




}
