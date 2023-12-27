package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mortal
 * @date 2023/12/26 22:02
 */

/**
 * 配置类，用于创建AliOssUtil对象（给这个工具类的属性字段赋值）
 */
@Configuration
@Slf4j
public class OssConfiguration {

	/**
	 * 当spring容器启动后，这个阿里云工具类对象，会自动被创建，
	 * 并且将AliOssProperties类中的属性值（有yml中获取），赋值给工具类的属性值
	 * @param aliOssProperties
	 * @return
	 */
	// 加了@Bean注解，spring启动之后，会自动调用这个方法
	@Bean // 让Spring容器管理我们自定义的Bean
	// @ConditionalOnMissingBean 的作用是：当 Spring 容器中不存在目标类型的 bean 时，才会创建该 bean。
	@ConditionalOnMissingBean //  是 Spring Boot 的一个条件注解
	// 以参数的形式，注入
	public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
		log.info("开始创建阿里云文件上传的工具类对象：{}", aliOssProperties);
		// 通过自定义的这个属性类：aliOssProperties，调用其get方法，
		// 拿到其对象属性 endpoint;accessKeyId等对象；
		// 然后返回给其AliOssUtil的有参构造器，创建其对象（调用有参构造器就是给其属性赋值了）；
		return new AliOssUtil(aliOssProperties.getEndpoint(),
				aliOssProperties.getAccessKeyId(),
				aliOssProperties.getAccessKeySecret(),
				aliOssProperties.getBucketName());


	}
}
