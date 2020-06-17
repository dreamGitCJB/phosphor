package org.linlinjava.litemall.db.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName : IntegralType
 * @Description : 积分类型
 * @Author : chenjinbao
 * @Date : 2020/6/14 1:52 下午
 * @Version 1.0.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralType {

	INVITE_NEW_MEMBERS(0,"邀请新人");

	@EnumValue
	private int code;
	private String name;

	IntegralType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
