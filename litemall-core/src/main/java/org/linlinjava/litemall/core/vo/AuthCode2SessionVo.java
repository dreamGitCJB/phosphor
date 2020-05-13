package org.linlinjava.litemall.core.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName : AuthCode2SessionVo
 * @Description : 微信登录返回VO
 * @Author : chenjinbao
 * @Date : 2020/5/4 8:33 PM
 * @Version 1.0.0
 */

@Data
public class AuthCode2SessionVo {

	/**
	 * 用户唯一标识
	 */
	private String openid;

	/**
	 * 会话密钥
	 */
	@JsonProperty("session_key")
	private String sessionKey;

	/**
	 * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
	 */
	@JsonProperty("unionid")
	private String unionId;

	/**
	 * 错误码
	 */
	private String errcode;

	/**
	 * 错误信息
	 */
	private String errmsg;
}
