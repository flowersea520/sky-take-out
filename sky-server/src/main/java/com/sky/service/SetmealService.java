package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

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

	/**
	 *  批量删除套餐
	 * @param ids
	 */
	void deleteBatch(List<Long> ids);

	/**
	 *   根据id查询套餐和套餐关联的菜品数据
	 * @param id
	 * @return
	 */
	SetmealVO getByIdWithDish(Long id);

	/**
	 *  修改套餐
	 * @param setmealDTO
	 */
	void update(SetmealDTO setmealDTO);

	/**
	 * 起售停售套餐
	 * @param status
	 * @param id
	 */
	void stopOrStop(Integer status, Long id);
}
