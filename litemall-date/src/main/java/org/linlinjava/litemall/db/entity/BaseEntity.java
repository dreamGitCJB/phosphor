package org.linlinjava.litemall.db.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName : BaseEntity
 * @Description : 实体类的基类
 * @Author : chenjinbao
 * @Date : 2020/5/25 2:26 下午
 * @Version 1.0.0
 */
@Data
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 6325673562L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 租户ID
	 */
	private String tenantId;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime addTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除
	 */
	@TableLogic
	private Boolean deleted;
}
