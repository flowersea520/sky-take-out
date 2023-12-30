package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/27 11:22
 */
public interface DishService {
	/**
	 * 新增菜品和对应口味；
	 * @param dishDTO
	 */
	void saveWithFlavor(DishDTO dishDTO);

	/**
	 *  菜品分页查询
	 * @return
	 */
	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

	/**
	 * 批量Batch删除菜品
	 * @param ids
	 */
	void deleteBatch(List<Long> ids);
}
