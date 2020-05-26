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
 * 意见反馈表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_feedback")
public class Feedback extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 反馈类型
     */
    private String feedType;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否含有图片
     */
    private Boolean hasPicture;

    /**
     * 图片地址列表，采用JSON数组格式
     */
    private String picUrls;




}
