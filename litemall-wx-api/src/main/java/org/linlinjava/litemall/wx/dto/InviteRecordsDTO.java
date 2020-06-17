package org.linlinjava.litemall.wx.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName : InviteRecordsDTO
 * @Description : 邀请
 * @Author : chenjinbao
 * @Date : 2020/6/17 8:17 下午
 * @Version 1.0.0
 */

@Data
public class InviteRecordsDTO {
	private Integer id;
	private String avatar;
	private LocalDateTime invitedTime;
	private BigDecimal integral;
}
