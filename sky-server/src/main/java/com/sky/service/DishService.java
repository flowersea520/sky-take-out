package com.sky.service;

import com.sky.dto.DishDTO;

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
}
