package org.linlinjava.litemall.db.entity;

import java.math.BigDecimal;

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
 * 专题表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_topic")
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专题标题
     */
    private String title;

    /**
     * 专题子标题
     */
    private String subtitle;

    /**
     * 专题内容，富文本格式
     */
    private String content;

    /**
     * 专题相关商品最低价
     */
    private BigDecimal price;

    /**
     * 专题阅读量
     */
    private String readCount;

    /**
     * 专题图片
     */
    private String picUrl;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 专题相关商品，采用JSON数组格式
     */
    @TableField(el = "goods typeHandler = JsonIntegerArrayTypeHandler.class")
    private Integer[] goods;

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
