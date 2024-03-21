package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author mortal
 * @date 2024/3/21 16:47
 */

/**
 * 当前配置类不是必须的，因为 Spring Boot 框架会自动装配 RedisTemplate 对象，但是默认的key序列化器为
 *
 * JdkSerializationRedisSerializer，导致我们存到Redis中后的数据和原始数据有差别，故设置为
 *
 * StringRedisSerializer序列化器。
 */
@Configuration
@Slf4j
public class RedisConfiguration {
	// RedisTemplate，我们可以直接对Redis进行各种操作，而无需关心底层的连接和序列化等细节。

	// redis相关的start，会有我们的 redis连接工厂对象的
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate();
		// 设置redis的连接工厂对象
		// 你将传入的redisConnectionFactory参数设置为redisTemplate的连接工厂。这个连接工厂负责创建与Redis服务器的连接。
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// 设置redis key 的序列化器
		// 为redisTemplate设置了键的序列化器。序列化是将Java对象转换为字节流的过程，以便可以将其存储在Redis中。
		// StringRedisSerializer，这意味着键将被序列化为字符串。因为Redis的键通常都是字符串。
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}
}
