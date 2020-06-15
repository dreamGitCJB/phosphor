package org.linlinjava.litemall.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import org.linlinjava.litemall.db.common.enums.IntegralType;
import org.linlinjava.litemall.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分纪录表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-06-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_integral_record")
@Builder
public class IntegralRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 积分类型
     */
    private IntegralType integralType;

	/**
	 * 来源ID，例：邀请用户就是被邀请的用户ID
	 */
	private Integer valueId;

    /**
     * 积分
     */
    private BigDecimal integral;


}
