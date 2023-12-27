package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    // 创建一个工具类，给其赋值
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传方法，该方法返回的是一个字符串的URL请求路径；
     *
     * @param bytes 字节数组；（将前端传过来的文件，转化为字节数组对象）
     * @param objectName  构造的新文件名称；
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
/**
 * StringBuilder 是 Java 中的一个类，它用于构建和操作可变长的字符串。
 * 与 String 类不同，StringBuilder 允许你直接修改字符串内容，
 * 而不需要创建新的字符串对象。这对于需要频繁修改字符串内容的场景特别有用，因为它可以显著提高性能。
 */
        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        // 这行代码创建了一个新的StringBuilder对象，并初始化其内容为字符串"https://"。
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
//当你使用 StringBuilder 来构建或修改字符串时，
// 你实际上是在操作一个字符数组。为了得到一个标准的字符串对象，你需要调用 toString() 方法
        log.info("文件上传到:{}", stringBuilder.toString()); // 输出引用对象的内容

        return stringBuilder.toString();
    }
}
