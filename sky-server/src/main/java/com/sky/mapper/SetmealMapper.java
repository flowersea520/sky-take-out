package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mortal
 * @date 2023/12/24 13:50
 */
@Mapper
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface SetmealMapper {

	/**
	 * 根据分类id查询套餐的数量
	 * @param categoryId
	 * @return
	 */
	@Select("select count(*) from setmeal where category_id = #{categoryId}")
	Integer countByCategoryId(Long categoryId);

	/**
	 * 根据id修改套餐的起售停售
	 * 遇到update和insert方法，就将自定义的填充注解加进去；这样公共字段就可以不用设置了
	 * @param setmeal
	 * 由于加了AutoFill自定义的公共字段填充注解，所以，那个公共字段就不用在service层里面set属性了
	 */
	@AutoFill(value = OperationType.UPDATE)
	void update(Setmeal setmeal);

	/**
	 * 新增套餐
	 * @param setmeal
	 */
	@AutoFill(value = OperationType.INSERT)
	void insert(Setmeal setmeal);

	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
