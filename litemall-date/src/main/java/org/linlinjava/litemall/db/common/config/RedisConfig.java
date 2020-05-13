package org.linlinjava.litemall.db.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName : RedisConfig
 * @Description : redis缓存
 * @Author : chenjinbao
 * @Date : 2020/5/4 8:48 PM
 * @Version 1.0.0
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		// 设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		// 配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer<?> stringSerializer = new StringRedisSerializer();
		// key序列化
		redisTemplate.setKeySerializer(stringSerializer);
		// value序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// Hash key序列化
		redisTemplate.setHashKeySerializer(stringSerializer);
		// Hash value序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
