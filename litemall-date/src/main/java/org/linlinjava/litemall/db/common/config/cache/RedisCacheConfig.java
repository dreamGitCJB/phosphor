package org.linlinjava.litemall.db.common.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @ClassName : RedisCacheConfig
 * @Description : redisCacheConfig
 * @Author : chenjinbao
 * @Date : 2020/5/27 10:53 下午
 * @Version 1.0.0
 */

@Slf4j
@Configuration
@AutoConfigureAfter(RedisCacheConfiguration.class)
public class RedisCacheConfig extends CachingConfigurerSupport {
	@Autowired
	LettuceConnectionFactory  connectionFactory;

	@Override
	public CacheManager cacheManager() {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
		//设置缓存的默认超时时间：30分钟
		redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L))
									.disableCachingNullValues()
									.disableKeyPrefix()
									.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
									.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((new GenericJackson2JsonRedisSerializer())));

		return RedisCacheManager.builder(RedisCacheWriter
				                .nonLockingRedisCacheWriter(connectionFactory))
		                .cacheDefaults(redisCacheConfiguration).build();
	}
}
