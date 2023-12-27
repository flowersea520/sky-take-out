package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mortal
 * @date 2023/12/24 13:50
 */
@Mapper
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface DishMapper {
	/**
	 * 根据分类id查询菜品数量
	 * @param categoryId
	 * @return
	 */
	@Select("select count(*) from dish where category_id = #{categoryId}")
	Integer countByCategoryId(Long categoryId);

	/**
	 * 新增菜品（插入菜品语句）
	 * @param dish
	 * 讲实体类中的属性值，赋值给数据库字段属性，即可
	 */
	@AutoFill(value = OperationType.INSERT) // 如果注解没有，拦截不到，自己手动给公共字段赋值（就是增强方法的效果）
	void insert(Dish dish);
}
