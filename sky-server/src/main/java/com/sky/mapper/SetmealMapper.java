package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author mortal
 * @date 2023/12/24 13:50
 */
@Mapper
public interface SetmealMapper {

	/**
	 * 根据分类id查询套餐的数量
	 * @param categoryId
	 * @return
	 */
	@Select("select count(*) from setmeal where category_id = #{categoryId}")
	Integer countByCategoryId(Long categoryId);
}