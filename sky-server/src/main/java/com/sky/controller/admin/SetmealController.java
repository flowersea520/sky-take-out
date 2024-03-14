package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

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

	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("套餐分页查询")
	public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
		log.info("套餐分页查询：{}", setmealPageQueryDTO);
		PageResult pageResult =  setmealService.pageQuery(setmealPageQueryDTO);
		return Result.success(pageResult);
	}


	/**
	 *  批量删除套餐
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("批量删除套餐")
	// 一般除了查询结果加泛型，其他不加，默认Object类
	public Result delete(@RequestParam List<Long> ids) {
		log.info("批量删除套餐的id: {}", ids);
		// 调用service层
		setmealService.deleteBatch(ids);
		return Result.success();
	}

	/**
	 *  根据id查询套餐和套餐关联的菜品数据，用于修改页面回显数据
	 * @return
	 */
	@GetMapping("{id}")  // 点击套餐的修改按钮，就会走这个路径，我们进行回显
	@ApiOperation("根据id查询套餐和套餐关联的菜品数据")
 	// VO：用于展示层与业务逻辑层之间的数据传输，关注数据的展示形式。
	public Result<SetmealVO> getById(@PathVariable Long id) {
		log.info("根据id查询套餐和套餐关联的菜品数据：{}", id);
		// 根据id查询套餐表
		SetmealVO setmealVo =  setmealService.getByIdWithDish(id);
		// 查询操作 肯定要返回一个 实体对象给前端的
		return Result.success(setmealVo);
	}

	/**
	 * 修改套餐
	 */
	@PutMapping
	@ApiOperation("修改套餐")
	public Result update(@RequestBody SetmealDTO setmealDTO) {
		// 调用业务层
		setmealService.update(setmealDTO);
		return Result.success();

	}

	/**
	 * 起售停售套餐
	 * @param status
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("起售停售套餐")
	// 注意：接口文档中除了有 路径参数要接收，还有一个 query查询参数要接收
	public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
		log.info("起售停售套餐:{}", status);
		// 调用业务层
		// 看接口文档是 前端向后端传递两个参数 一个路径参数status，和请求参数query（id），记得都写在业务层上
		setmealService.stopOrStop(status,id);
		return Result.success();
	}


}
