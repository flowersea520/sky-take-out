package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/24 14:45
 */
public interface CategoryService {
	/**
	 * 新增分类
	 * @param categoryDTO
	 */
	void save(CategoryDTO categoryDTO);

	/**
	 * 分类分页查询
	 * @return
	 */
	PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

	/**
	 * 根据Id删除分类
	 */
	void deleteById(Long id);

	/**
	 * 修改分类
	 * @param categoryDTO
	 */

	void updateCategory(CategoryDTO categoryDTO);

	/**
	 * 启用、禁用分类
	 * @param status
	 * @param id
	 */
	void startOrStop(Integer status, Long id);

	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	List<Category> list(Integer type);
}
