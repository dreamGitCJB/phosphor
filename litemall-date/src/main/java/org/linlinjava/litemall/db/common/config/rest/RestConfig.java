package org.linlinjava.litemall.db.common.config.rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName : RestConfig
 * @Description : RestTemplate
 * @Author : chenjinbao
 * @Date : 2020/5/4 8:43 PM
 * @Version 1.0.0
 */

@Configuration
public class RestConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.build();
		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());

		return restTemplate;
	}
}
