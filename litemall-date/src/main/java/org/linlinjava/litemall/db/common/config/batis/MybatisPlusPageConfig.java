package org.linlinjava.litemall.db.common.config.batis;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.linlinjava.litemall.db.common.util.ObjectUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

/**
 * @ClassName : MybatisPlusConfig
 * @Description :
 * @Author : chenjinbao
 * @Date : 2020/5/4 11:06 PM
 * @Version 1.0.0
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusPageConfig {
	/**
	 * 分页拦截器
	 */
	@Bean
	public BladePaginationInterceptor paginationInterceptor(ObjectProvider<QueryInterceptor[]> queryInterceptors,
															ObjectProvider<ISqlParser[]> sqlParsers,
															ObjectProvider<ISqlParserFilter> sqlParserFilter) {
		BladePaginationInterceptor paginationInterceptor = new BladePaginationInterceptor();
		QueryInterceptor[] queryInterceptorArray = queryInterceptors.getIfAvailable();
		if (ObjectUtil.isNotEmpty(queryInterceptorArray)) {
			AnnotationAwareOrderComparator.sort(queryInterceptorArray);
			paginationInterceptor.setQueryInterceptors(queryInterceptorArray);
		}
		ISqlParser[] sqlParsersArray = sqlParsers.getIfAvailable();
		if (ObjectUtil.isNotEmpty(sqlParsersArray)) {
			paginationInterceptor.setSqlParserList(Arrays.asList(sqlParsersArray));
		}
		paginationInterceptor.setSqlParserFilter(sqlParserFilter.getIfAvailable());
		return paginationInterceptor;
	}

	/**
	 * sql 注入
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new PolarisSqlInjector();
	}
}
