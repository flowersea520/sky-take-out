package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author mortal
 * @date 2024/3/21 23:22
 */
//手动设置 Bean的名称，防止冲突
@RestController("userShopController")
@RequestMapping("/user/shop")  // 访问的是用户端，不是管理端
@Api(tags = "店铺相关接口")
@Slf4j
// 默认这个Bean的名称是以类名首字母小写命名的，所以我们会与admin管理端的Bean冲突，要手动设置
public class ShopController {
	// Redis最常被用作 缓存层， 它可以存储应用程序经常访问的数据，以减少对数据库的访问，从而提高性能。
	// 例如，你可以将用户会话信息、热点数据或计算密集型结果存储在Redis中，以减少对后端数据库的请求。
	@Autowired
	private RedisTemplate redisTemplate;

	// 多次用到这个常量，我们将其提取出来
	public static final String  KEY = "SHOP_STATUS";


	/**
	 * 获取到店铺的营业状态:  （ 用户端我们只能给予获取营业状态的方法）
	 * 店铺营业状态：1为营业，0为打烊
	 *
	 * @return
	 */
	@GetMapping("/status")
	@ApiOperation("获取营业状态")
	public Result<Integer> getStatus() {
		// 通过redis缓存对象，获取其缓存中的 状态值（通过redis的key）
		// 放的时候什么类型，取的时候也是什么类型
		Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
		log.info("获取到店铺的营业状态为： {}", shopStatus == 1 ? "营业中" : "打样中");
		return Result.success(shopStatus);
	}


}
