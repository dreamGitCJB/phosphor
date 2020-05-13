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
 * 文件存储表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_storage")
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件的唯一索引
     */
    private String key;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件访问链接
     */
    private String url;

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
