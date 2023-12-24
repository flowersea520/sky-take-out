package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/24 13:46
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	/**
	 * 新增分类
	 * @param categoryDTO
	 * @return
	 */
	@ApiOperation("新增分类")
	@PostMapping // 只要不是查询操作要返回对象给前端，其余情况没必要没泛型
	public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
		log.info("新增分类：{}", categoryDTO);
		categoryService.save(categoryDTO);
		return Result.success();
	}

	/**
	 *  分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("分类分页查询")
	// 这个泛型写的是：接口文档中要返回给前端的类型；这里就是定义的PageResult
	public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) { // 根据接口文档要求，提供多个DTO
		log.info("分页查询：{}", categoryPageQueryDTO);
		PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
		return Result.success(pageResult);

	}

	/**
	 *  根据id删除分类
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@ApiOperation("根据id删除分类")
	public Result deleteById(Long id) {
		log.info("根据删除分类：{}", id);
		categoryService.deleteById(id);
		return Result.success();
	}

	/**
	 * 修改分类
	 * @param categoryDTO
	 * @return
	 */
	@PutMapping
	@ApiOperation("修改分类")
	public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
		log.info("修改分类：{}", categoryDTO);
		categoryService.updateCategory(categoryDTO);
		return Result.success();
	}

	/**
	 * 启用、禁用分类
	 * @param status
	 * @return
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("启用、禁用分类")
	public Result startOrStop(@PathVariable Integer status, Long id) {
		log.info("启用、禁用分类: {}", status);
		categoryService.startOrStop(status,id);
		return Result.success();
	}

	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	@ApiOperation("根据类型查询分类")
	@GetMapping("/list")
	public Result<List<Category>> list(Integer type) {
		List<Category> lists = categoryService.list(type);
		return Result.success(lists);

	}





}
