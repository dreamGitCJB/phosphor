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
import org.linlinjava.litemall.db.common.handler.JsonIntegerArrayTypeHandler;
import org.linlinjava.litemall.db.common.handler.JsonStringArrayTypeHandler;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_admin")
public class Admin extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 管理员名称
     */
    private String username;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 最近一次登录IP地址
     */
    private String lastLoginIp;

    /**
     * 最近一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 头像图片
     */
    private String avatar;


    /**
     * 角色列表
     */

    @TableField(typeHandler = JsonIntegerArrayTypeHandler.class)
    private Integer[] roleIds;


}
