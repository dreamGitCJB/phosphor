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
 * 通知管理员表
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("litemall_notice_admin")
public class NoticeAdmin extends BaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 通知ID
     */
    private Integer noticeId;

    /**
     * 通知标题
     */
    private String noticeTitle;

    /**
     * 接收通知的管理员ID
     */
    private Integer adminId;

    /**
     * 阅读时间，如果是NULL则是未读状态
     */
    private LocalDateTime readTime;




}
