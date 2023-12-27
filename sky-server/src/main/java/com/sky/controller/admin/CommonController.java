package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author mortal
 * @date 2023/12/25 22:16
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

	@Autowired
	private AliOssUtil aliOssUtil;
	/**
	 * MultipartFile 是 Spring Framework 中用于处理文件上传的接口。
	 * 当一个 HTTP 请求包含一个 multipart/form-data 类型的 POST 请求时，
	 * 该请求中的文件可以通过 MultipartFile 接口来处理。
	 * @param file 形参名要和前端传过来的变量名一致；
	 * @return
	 */
	@ApiOperation("文件上传")
	@PostMapping ("/upload")
	public Result<String> upload(MultipartFile file) { // MultipartFile 处理文件的接口
		log.info("文件上传：{}", file);

		try {
			// 原始文件名；
			String originalFilename = file.getOriginalFilename();
			// 截取原始文件名的后缀；  dfdfdf.png  将这个.png截取出来
			// String 类的 lastIndexOf 方法
			// 在 Java 中用于查找指定字符或字符串在另一个字符串中最后一次出现的位置。(返回其索引值）
			int lastIndexOf = originalFilename.lastIndexOf("."); // 这个点 . 最后一次出现的位置
			// String 类的 substring 方法在 Java 中用于获取字符串的 子字符串。
			// 拿到extension扩展名（包括小数点）
			String extension = originalFilename.substring(lastIndexOf);// 从指定索引(包含），一直截取到末尾；
			/**
			 * UUID.randomUUID().toString() 是 Java 代码，
			 * 它用于生成一个随机的、唯一的标识符（UUID，即通用唯一标识符）。
			 * toString()将随机生成的 UUID 转换为其字符串形式。
			 */
			// 构造新文件名；
			String ObjectName = UUID.randomUUID().toString() + extension;


			// 调用自定义的阿里云工具类的update方法，实现文件上传（该方法返回的是一个字符串的URL请求路径；）
			String filePath = aliOssUtil.upload(file.getBytes(), ObjectName);
		} catch (IOException e) {
			log.error("文件上传失败：{}", e);
		}
		// 返回文件上传失败，给前端；
		return Result.error(MessageConstant.UPLOAD_FAILED);
	}

}
