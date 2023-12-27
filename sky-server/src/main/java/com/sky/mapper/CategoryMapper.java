package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/24 13:49
 */

@Mapper
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface  CategoryMapper {
	/**
	 *  新增分类；插入数据
	 * @param category
	 */
	// id不用传；这里相当于是：将实体类中的数据，映射到数据库中去；
	@Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
			"VALUES (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})") //

	void insert(Category category);

	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

	/**
	 *  根据id删除分类
	 * @param id
	 */
	@Delete("delete from category where id = #{id}")
	void deleteById(Long id);

	/**
	 * 修改分类
	 * 和
	 *  启用和禁用分类
	 *  共用一个mapper接口
	 */
	void update(Category category);

	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */

	List<Category> list(Integer type);
}
