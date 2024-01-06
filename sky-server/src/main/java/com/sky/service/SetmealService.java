package com.sky.service;

import com.sky.dto.SetmealDTO;

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
}
