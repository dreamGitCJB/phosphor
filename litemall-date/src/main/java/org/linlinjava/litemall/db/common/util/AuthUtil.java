/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.linlinjava.litemall.db.common.util;


import org.linlinjava.litemall.db.common.constans.CommonConstant;
import org.linlinjava.litemall.db.common.constans.StringPool;
import org.linlinjava.litemall.db.common.constans.TokenConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Auth工具类
 *
 * @author Chill
 */
public class AuthUtil {
	private static final String BLADE_USER_REQUEST_ATTR = "_BLADE_USER_REQUEST_ATTR_";

	private final static String HEADER = TokenConstant.HEADER;
	private final static String ACCOUNT = TokenConstant.ACCOUNT;
	private final static String USER_NAME = TokenConstant.USER_NAME;
	private final static String NICK_NAME = TokenConstant.NICK_NAME;
	private final static String USER_ID = TokenConstant.USER_ID;
	private final static String DEPT_ID = TokenConstant.DEPT_ID;
	private final static String ROLE_ID = TokenConstant.ROLE_ID;
	private final static String ROLE_NAME = TokenConstant.ROLE_NAME;
	private final static String TENANT_ID = TokenConstant.TENANT_ID;
	private final static String CLIENT_ID = TokenConstant.CLIENT_ID;

//	/**
//	 * 获取用户信息
//	 *
//	 * @return BladeUser
//	 */
//	public static BladeUser getUser() {
//		HttpServletRequest request = WebUtil.getRequest();
//		if (request == null) {
//			return null;
//		}
//		// 优先从 request 中获取
//		Object bladeUser = request.getAttribute(BLADE_USER_REQUEST_ATTR);
//		if (bladeUser == null) {
//			bladeUser = getUser(request);
//			if (bladeUser != null) {
//				// 设置到 request 中
//				request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
//			}
//		}
//		return (BladeUser) bladeUser;
//	}
//
//	/**
//	 * 获取用户信息
//	 *
//	 * @param request request
//	 * @return BladeUser
//	 */
//	public static BladeUser getUser(HttpServletRequest request) {
//		Claims claims = getClaims(request);
//		if (claims == null) {
//			return null;
//		}
//		String clientId = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.CLIENT_ID));
//		Long userId = Func.toLong(claims.get(org.springblade.core.secure.utils.AuthUtil.USER_ID));
//		String tenantId = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.TENANT_ID));
//		String deptId = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.DEPT_ID));
//		String roleId = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.ROLE_ID));
//		String account = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.ACCOUNT));
//		String roleName = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.ROLE_NAME));
//		String userName = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.USER_NAME));
//		String nickName = Func.toStr(claims.get(org.springblade.core.secure.utils.AuthUtil.NICK_NAME));
//		BladeUser bladeUser = new BladeUser();
//		bladeUser.setClientId(clientId);
//		bladeUser.setUserId(userId);
//		bladeUser.setTenantId(tenantId);
//		bladeUser.setAccount(account);
//		bladeUser.setDeptId(deptId);
//		bladeUser.setRoleId(roleId);
//		bladeUser.setRoleName(roleName);
//		bladeUser.setUserName(userName);
//		bladeUser.setNickName(nickName);
//		return bladeUser;
//	}

	/**
	 * 是否为超管
	 *
	 * @return boolean
	 */
	public static boolean isAdministrator() {
		//TODO 判断是否是超管
//		return StringUtil.containsAny(getUserRole(), RoleConstant.ADMINISTRATOR);
		return false;
	}
//
//	/**
//	 * 获取用户id
//	 *
//	 * @return userId
//	 */
//	public static Long getUserId() {
//		BladeUser user = getUser();
//		return (null == user) ? -1 : user.getUserId();
//	}
//
//	/**
//	 * 获取用户id
//	 *
//	 * @param request request
//	 * @return userId
//	 */
//	public static Long getUserId(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? -1 : user.getUserId();
//	}
//
//	/**
//	 * 获取用户账号
//	 *
//	 * @return userAccount
//	 */
//	public static String getUserAccount() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getAccount();
//	}
//
//	/**
//	 * 获取用户账号
//	 *
//	 * @param request request
//	 * @return userAccount
//	 */
//	public static String getUserAccount(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getAccount();
//	}
//
//	/**
//	 * 获取用户名
//	 *
//	 * @return userName
//	 */
//	public static String getUserName() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getUserName();
//	}
//
//	/**
//	 * 获取用户名
//	 *
//	 * @param request request
//	 * @return userName
//	 */
//	public static String getUserName(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getUserName();
//	}
//
//	/**
//	 * 获取昵称
//	 *
//	 * @return userName
//	 */
//	public static String getNickName() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getNickName();
//	}
//
//	/**
//	 * 获取昵称
//	 *
//	 * @param request request
//	 * @return userName
//	 */
//	public static String getNickName(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getNickName();
//	}
//
//	/**
//	 * 获取用户部门
//	 *
//	 * @return userName
//	 */
//	public static String getDeptId() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getDeptId();
//	}
//
//	/**
//	 * 获取用户部门
//	 *
//	 * @param request request
//	 * @return userName
//	 */
//	public static String getDeptId(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getDeptId();
//	}
//
//	/**
//	 * 获取用户角色
//	 *
//	 * @return userName
//	 */
//	public static String getUserRole() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getRoleName();
//	}
//
//	/**
//	 * 获取用角色
//	 *
//	 * @param request request
//	 * @return userName
//	 */
//	public static String getUserRole(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getRoleName();
//	}

	/**
	 * 获取租户ID
	 *
	 * @return tenantId
	 */
	public static String getTenantId() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getTenantId();
		String header = WebUtil.getHeader(TokenConstant.TENANT_HEAD);
		return Func.toStr(header, StringPool.EMPTY);

	}

//	/**
//	 * 获取租户ID
//	 *
//	 * @param request request
//	 * @return tenantId
//	 */
//	public static String getTenantId(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getTenantId();
//	}

//	/**
//	 * 获取客户端id
//	 *
//	 * @return clientId
//	 */
//	public static String getClientId() {
//		BladeUser user = getUser();
//		return (null == user) ? StringPool.EMPTY : user.getClientId();
//	}
//
//	/**
//	 * 获取客户端id
//	 *
//	 * @param request request
//	 * @return clientId
//	 */
//	public static String getClientId(HttpServletRequest request) {
//		BladeUser user = getUser(request);
//		return (null == user) ? StringPool.EMPTY : user.getClientId();
//	}
//
//	/**
//	 * 获取Claims
//	 *
//	 * @param request request
//	 * @return Claims
//	 */
//	public static Claims getClaims(HttpServletRequest request) {
//		String auth = request.getHeader(org.springblade.core.secure.utils.AuthUtil.HEADER);
//		if (StringUtil.isNotBlank(auth)) {
//			String token = JwtUtil.getToken(auth);
//			if (StringUtil.isNotBlank(token)) {
//				return org.springblade.core.secure.utils.AuthUtil.parseJWT(token);
//			}
//		} else {
//			String parameter = request.getParameter(org.springblade.core.secure.utils.AuthUtil.HEADER);
//			if (StringUtil.isNotBlank(parameter)) {
//				return org.springblade.core.secure.utils.AuthUtil.parseJWT(parameter);
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 获取请求头
//	 *
//	 * @return header
//	 */
//	public static String getHeader() {
//		return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
//	}
//
//	/**
//	 * 获取请求头
//	 *
//	 * @param request request
//	 * @return header
//	 */
//	public static String getHeader(HttpServletRequest request) {
//		return request.getHeader(HEADER);
//	}
//
//	/**
//	 * 解析jsonWebToken
//	 *
//	 * @param jsonWebToken jsonWebToken
//	 * @return Claims
//	 */
//	public static Claims parseJWT(String jsonWebToken) {
//		return JwtUtil.parseJWT(jsonWebToken);
//	}

}
