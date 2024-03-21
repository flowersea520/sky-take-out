package com.sky.controller.admin;

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
// 手动设置bean的名称，防止冲突
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

	// Redis最常被用作 缓存层， 它可以存储应用程序经常访问的数据，以减少对数据库的访问，从而提高性能。
	// 例如，你可以将用户会话信息、热点数据或计算密集型结果存储在Redis中，以减少对后端数据库的请求。
	@Autowired
	private RedisTemplate redisTemplate;

	// 多次用到这个常量，我们将其提取出来
	public static final String  KEY = "SHOP_STATUS";


	/**
	 * 设置店铺的营业状态
	 *
	 * @param status
	 * @return
	 */
	// 接口文档的返回结果中，没有规定data的类型，所以我们这里的result就不加泛型
	@PutMapping("/{status}") // 这个 {} 表示动态的路径参数
	@ApiOperation("设置店铺的营业状态")
	public Result setStatus(@PathVariable Integer status) {
		log.info("设置店铺的营业状态: {}", status == 1 ? "营业中" : "打样中");
		// 以减少对后端数据库的请求。我们这里使用redis操作（这里操作redis的key是字符串类型的数据结构）
		// 注意：这里将 status放入这个reids时：是 Integer类型
		redisTemplate.opsForValue().set(KEY, status); // 在redis中添加 键 和 值
		return Result.success();
	}

	/**
	 * 获取到店铺的营业状态:
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
