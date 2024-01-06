package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

	/**
	 *  根据id查询菜品和对应的口味数据
	 * @param id
	 * @return
	 */
	DishVO getByIdWithFlavor(Long id);

	/**
	 * 修改菜品基本信息和对应口味信息
	 * @param dishDTO
	 * @return
	 */
	void updateWithFlavor(DishDTO dishDTO);

	/**
	 * 菜品起售、停售
	 * @param status
	 * @param id
	 */
	void startOrStop(Integer status, Long id);

	/**
	 * 根据分类id查询菜品
	 * @param categoryId
	 * @return
	 */
	List<Dish> list(Long categoryId);
}
