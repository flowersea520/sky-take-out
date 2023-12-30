package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

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
	/**
	 * 菜品分页查询
	 * @param dishPageQueryDTO
	 *
	 */
	Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

	/**
	 *根据主键id，查询菜品
	 * @param id
	 * @return
	 * #{id}是一个参数占位符，它会被传递给方法的参数值所替换。
	 */
	@Select("select * from dish where id = #{id}")
	Dish getById(Long id);

	/**
	 *  根据主键删除菜品数据
	 * @param id
	 */
	@Delete("delete from dish where id = #{id}")
	void deleteById(Long id);

	/**
	 *  根据菜品id集合批量删除菜品数据
	 * @param ids
	 */
	void deleteByIds(List<Long> ids);

	/**
	 *  修改菜品基本信息和对应口味信息
	 * @param dish
	 */
	// 只要用到  修改和插入的操作，就使用自定义的注解切面，进行公共字段填充
	@AutoFill(value = OperationType.UPDATE)
	void update(Dish dish);
}
