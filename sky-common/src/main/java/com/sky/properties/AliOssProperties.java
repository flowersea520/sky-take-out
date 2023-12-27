package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// @ConfigurationProperties是Spring Boot提供的一个注解，用于读取配置文件（这里是yml）并将其映射到Java对象上。
// 它主要用于将配置文件中的属性值  注入到Java对象的属性字段、构造函数参数或Setter方法中。
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    // 阿里云的Endpoint是指阿里云账号的唯一访问地址，
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
