/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
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
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package org.linlinjava.litemall.db.common.config.batis;

import org.apache.ibatis.plugin.Invocation;
import org.linlinjava.litemall.db.common.util.ObjectUtil;

/**
 * 查询拦截器执行器
 *
 * <p>
 * 目的：抽取此方法是为了后期方便同步更新 {@link BladePaginationInterceptor}
 * </p>
 *
 * @author L.cm
 */
public class QueryInterceptorExecutor {

	/**
	 * 执行查询拦截器
	 *
	 * @param interceptors 拦截器
	 * @param invocation   拦截器参数
	 */
	static void exec(QueryInterceptor[] interceptors, Invocation invocation) throws Throwable {
		if (ObjectUtil.isEmpty(interceptors)) {
			return;
		}
		for (QueryInterceptor interceptor : interceptors) {
			interceptor.intercept(invocation);
		}
	}

}
