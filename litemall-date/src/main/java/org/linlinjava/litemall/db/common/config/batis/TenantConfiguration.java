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

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore({MybatisPlusPageConfig.class})
@MapperScan("org.linlinjava.litemall.db.mapper")
public class TenantConfiguration {


	/**
	 * 自定义租户处理器
	 *
	 * @return TenantHandler
	 */
	@Bean
	@ConditionalOnMissingBean(TenantHandler.class)
	public TenantHandler bladeTenantHandler() {
		return new PolarisTenantHandler();
	}

	/**
	 * 租户 sql parser
	 *
	 * @param tenantHandler 多租户处理类
	 * @return TenantSqlParser
	 */
	@Bean
	@ConditionalOnMissingBean(PolarisTenantSqlParser.class)
	public PolarisTenantSqlParser tenantSqlParser(TenantHandler tenantHandler) {
		PolarisTenantSqlParser tenantSqlParser = new PolarisTenantSqlParser();
		tenantSqlParser.setTenantHandler(tenantHandler);
		return tenantSqlParser;
	}

//	/**
//	 * 自定义租户id生成器
//	 *
//	 * @return TenantId
//	 */
//	@Bean
//	@ConditionalOnMissingBean(TenantId.class)
//	public TenantId tenantId() {
//		return new BladeTenantId();
//	}

}
