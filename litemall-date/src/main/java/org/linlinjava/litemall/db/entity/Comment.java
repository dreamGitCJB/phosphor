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
 * 评论表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 如果type=0，则是商品评论；如果是type=1，则是专题评论。
     */
    private Integer valueId;

    /**
     * 评论类型，如果type=0，则是商品评论；如果是type=1，则是专题评论；
     */
    private Integer type;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 管理员回复内容
     */
    private String adminContent;

    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 是否含有图片
     */
    private Boolean hasPicture;

    /**
     * 图片地址列表，采用JSON数组格式
     */
    private String picUrls;

    /**
     * 评分， 1-5
     */
    private Integer star;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


}
