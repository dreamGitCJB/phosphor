package org.linlinjava.litemall.wx.dto;

import io.swagger.models.auth.In;
import lombok.Data;
import org.linlinjava.litemall.db.entity.IntegralRecord;

import java.math.BigDecimal;

@Data
public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Integer gender;
	/**
	 * 是否第一次登录
	 */
	private Boolean firstLogin;

	private BigDecimal integral;
}
