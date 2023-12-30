package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/27 12:29
 */
@Mapper
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface DishFlavorMapper {
	/**
	 *  批量插入口味数据
	 * @param flavors
	 */
	 void insertBatch(List<DishFlavor> flavors);

	/**
	 *  根据菜品Id删除对应的口味数据
	 * @param dishId
	 */
	@Delete("delete from dish_flavor where dish_id = #{dishId}")
	void deleteByDishId(Long dishId);

	/**
	 * 根据菜品Id集合批量删除关联口味数据
	 * @param ids
	 */
	void deleteByDishIds(List<Long> ids);

	/**
	 *  根据菜品id查询口味数据  (会查到多种口味，可以看数据库）
	 * @return
	 */
	List<DishFlavor> getByDishId(Long dishId);
}
