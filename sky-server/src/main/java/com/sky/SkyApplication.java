package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
// @EnableTransactionManagement 是 Spring 框架中用于启用声明式事务管理的注解。
// 当你在 Spring 配置类上使用这个注解时，
// Spring 会自动配置事务管理器，并允许你在应用程序中使用 @Transactional 注解来声明事务
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
