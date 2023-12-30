package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/27 11:19
 */
@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {
	@Autowired
	private DishService dishService;

	/**
	 *  新增菜品
	 * @param dishDTO
	 * @return
	 */
	@PostMapping
	@ApiOperation("新增菜品")
	public Result save(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品：{}", dishDTO);
		dishService.saveWithFlavor(dishDTO);
		return Result.success();
	}


	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 * @return
	 */
	@ApiOperation("菜品分页查询")
	@GetMapping("/page")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
		// 每次写完一个Controller层的方法，就输出日志log；
		log.info("菜品分页查询：{}", dishPageQueryDTO);
		PageResult pageResult =  dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}


	/**
	 * 批量删除菜品
	 * @param ids
	 * @return
	 */
	@ApiOperation("批量删除菜品")
	@DeleteMapping
	// @RequestParam是SpringMVC框架中的一个注解，主要用于将请求参数绑定到方法的参数上。
	// 在SpringMVC中的该注解将url中的参数绑定到该形参中去；
	// 前端和后端约定好了，传过来的URL查询参数是字符串类型 前端URL：？ids = 1，2，3；
	// 从请求中获取名为 ids 的参数，并将其值（通常是一个逗号分隔的字符串）转换为 List<Long> 类型。
	public Result delete(@RequestParam List<Long> ids) {
		log.info("菜品的批量删除：{}", ids);
		dishService.deleteBatch(ids);
		return Result.success();
	}


	/**
	 * 根据id查询菜品和对应的口味数据（前端都是通过id进行页面回显 -- 点击修改按钮，进行页面回显）
	 * @param id
	 * @return
	 */
	@ApiOperation("根据id查询菜品")
	@GetMapping("/{id}")
	public Result<DishVO> getDishById(@PathVariable Long id) {
		log.info("根据id查询菜品：{}", id);
		DishVO dishVO =  dishService.getByIdWithFlavor(id);
		return Result.success(dishVO);
	}

	/**
	 * 修改菜品基本信息和对应口味信息
	 * @param dishDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改菜品")
	public Result update(@RequestBody DishDTO dishDTO) {
		log.info("修改菜品：{}", dishDTO);
		dishService.updateWithFlavor(dishDTO);

		return Result.success();
	}

}
