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
package org.linlinjava.litemall.db.common.config.batis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

import java.sql.Connection;

/**
 * 拓展分页拦截器
 *
 * @author Chill
 */
@Setter
@Accessors(chain = true)
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class BladePaginationInterceptor extends PaginationInterceptor {

	@Bean
	public RequestContextListener requestContextListenerBean() {
		return new RequestContextListener();
	}

	/**
	 * 查询拦截器
	 */
	private QueryInterceptor[] queryInterceptors;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 查询拦截器
		QueryInterceptorExecutor.exec(queryInterceptors, invocation);
		return super.intercept(invocation);
	}

}
