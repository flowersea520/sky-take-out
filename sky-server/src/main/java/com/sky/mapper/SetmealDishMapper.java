package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/30 13:48
 */

/**
 *  这个mapper类也是映射着那个 套餐和菜品的第三张表；
 */
@Mapper
public interface SetmealDishMapper {
	/**
	 *  根据菜品id查询对应的套餐Id；
	 * @param dishIds
	 * @return
	 */
	// select setmeal_id from setmeal_dish where dish_id in (1,2,3)
//	这个查询将返回所有dish_id为1、2或3的记录的setmeal_id。
	// in() 有点类似于 or  ；  只要满足就返回；
	List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

	/**
	 * 批量保存套餐和菜品的关联关系
	 * @param setmealDishes
	 */
	void insertBatch(List<SetmealDish> setmealDishes);
}
