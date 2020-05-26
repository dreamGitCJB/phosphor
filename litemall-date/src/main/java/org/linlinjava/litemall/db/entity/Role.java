package org.linlinjava.litemall.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 角色表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 是否启用
     */
    private Boolean enabled;




}
