package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author mortal
 * @date 2024/1/6 17:05
 */
public interface SetmealService {
	/**
	 * 新增套餐
	 * @param setmealDTO
	 */
	void saveWithDish(SetmealDTO setmealDTO);

	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
