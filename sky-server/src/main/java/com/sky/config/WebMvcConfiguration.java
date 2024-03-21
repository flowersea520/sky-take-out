package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

	@Autowired
	private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

	/**
	 * 注册自定义拦截器
	 *
	 * @param registry
	 */
	protected void addInterceptors(InterceptorRegistry registry) {
		log.info("开始注册自定义拦截器...");
		registry.addInterceptor(jwtTokenAdminInterceptor)
				.addPathPatterns("/admin/**")
				.excludePathPatterns("/admin/employee/login");
	}

	/**
	 * 通过knife4j（读奈夫4j）生成接口文档
	 *
	 * @return
	 */
	@Bean
	public Docket docket1() {
		log.info("准备生成接口文档。。。");
		ApiInfo apiInfo = new ApiInfoBuilder()
				.title("苍穹外卖项目接口文档")
				.version("2.0")
				.description("苍穹外卖项目接口文档")
				.build();
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				// 调用 groupName方法，进行分组（每个组对应一个接口文档）
				.groupName("管理端接口")
				.apiInfo(apiInfo)
				.select()
				// 指定扫描到的包（扫描controller之后通过反射，生成接口文档）
				// 这里分开扫描（可以区分admin和user）
				.apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}

	@Bean
	public Docket docket() {
		log.info("准备生成接口文档。。。");
		ApiInfo apiInfo = new ApiInfoBuilder()
				.title("苍穹外卖项目接口文档")
				.version("2.0")
				.description("苍穹外卖项目接口文档")
				.build();
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				// 调用 groupName方法，进行分组（每个组对应一个接口文档）
				.groupName("用户端接口")
				.apiInfo(apiInfo)
				.select()
				// 指定扫描到的包（扫描controller之后通过反射，生成接口文档）
				// 这里分开扫描（可以区分admin和user）
				.apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}


	/**
	 * 设置静态资源映射（如果不搞这个静态映射，springmvc以为我们请求的是某个controller层的接口）
	 * addResourceHandlers（）该方法是重写了父类WebMvcConfigurationSupport的方法
	 *
	 * @param registry
	 */
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("开始设置静态资源映射。。。");
		registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * 扩展SpringMVC框架的消息转化器
	 *
	 * @param converters 转换器 的意思
	 *                   消息转换器是Spring MVC中用于将请求参数转换为Java对象（例如，将请求体中的JSON转换为Java对象）的组件
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) { // 这个就是消息转换器的
		log.info("扩展消息转换器。。。");
		// 创建一个消息转换器对象（将请求的json参数转换为java对象）
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		// 需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为JSON对象
		//ObjectMapper可以将Java对象序列化成JSON字符串，也可以将JSON字符串反序列化成Java对象。
		converter.setObjectMapper(new JacksonObjectMapper());
		// 将自己的消息转换器加入到容器中去（这里要设置优先级，要不然默认执行自带的转换器）
		converters.add(0, converter);
	}
}
