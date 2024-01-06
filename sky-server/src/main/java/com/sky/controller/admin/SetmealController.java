package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mortal
 * @date 2024/1/6 17:00
 */

/**
 *  套餐管理的Controller层
 *  注意：套餐setmeal和菜品dish是多对对的关系，一个套餐可能有多个菜品；一个菜品可能有多个套餐；所以会有一个关联表setmeal_dish
 */
@Api(tags = "套餐相关接口")
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
	@Autowired
	private SetmealService setmealService;

	/**
	 * 新增套餐
	 * @param setmealDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增套餐")
	public Result save(@RequestBody SetmealDTO setmealDTO) {
		log.info("新增套餐：{}", setmealDTO);
		setmealService.saveWithDish(setmealDTO);
		return Result.success();
	}
}
