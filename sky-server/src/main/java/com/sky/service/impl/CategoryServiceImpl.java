package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mortal
 * @date 2023/12/24 14:45
 */

/**
 *  分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	// 查询当前分类是否关联了菜品，如果关联了就抛出业务异常；
	// 所以注入dishMapper
	@Autowired
	private DishMapper dishMapper;
	// 查询当前分类是否关联了套餐setmeal；如果关联了套餐就抛出业务异常
	// 所以注入SetmealMapper
	@Autowired
	private SetmealMapper setmealMapper;


	/**
	 * 新增分类
	 * @param categoryDTO
	 *  传入的参数DTO最终都要拷贝成entity
	 */
	@Override
	public void save(CategoryDTO categoryDTO) {
		Category category = new Category();
		// 对象属性拷贝，将DTO拷贝到实体类entity
		BeanUtils.copyProperties(categoryDTO, category);

		// 将DTO缺少的属性，手动添加
		// 将分类状态默认为禁用状态0；
		category.setStatus(StatusConstant.DISABLE);

		// 设置创建时间、修改时间、创建人、修改人
		category.setCreateTime(LocalDateTime.now());
		category.setUpdateTime(LocalDateTime.now());
		// 创建人：一般用局部的线程Id
		category.setCreateUser(BaseContext.getCurrentId());
		category.setUpdateUser(BaseContext.getCurrentId());

		// 调用Mapper层
		categoryMapper.insert(category);

	}

	/**
	 * 分类分页查询
	 * @param categoryPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
		// 分页查询就用那个PageHelper插件（方法名的实参就是DTO中的page，和pageSize）
		PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
		// 下一条sql进行分页，自动加入limit关键字分页
		Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
		return new PageResult(page.getTotal(),page.getResult());
	}

	/**
	 *  根据Id删除分类
	 * @param id
	 */
	@Override
	public void deleteById(Long id) {
		// 查询当前分类是否关联了菜品，如果关联了就抛出业务异常；
		Integer count = dishMapper.countByCategoryId(id); // 根据分类id查询菜品数量
		if (count >0) {
			// 当前分类下有菜品，不能删除（抛出自定义的异常警告）
			// 这里定义了有参信息
			throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
		}

		// 查询当前分类是否关联了套餐setmeal；如果关联了套餐就抛出业务异常
		count = setmealMapper.countByCategoryId(id);
		if (count > 0) {
			//当前分类下有菜品，不能删除
			throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
		}
		// 删除分类数据；
		categoryMapper.deleteById(id);
	}

	/**
	 * 修改分类
	 * @param categoryDTO
	 */
	@Override
	public void updateCategory(CategoryDTO categoryDTO) {
		// 修改分类操作：他们都会改变所有数据库中所有字段的内容；
		// 所以进行将DTO转换为entity
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);
		// 剩下的属性单独设置
		// 这里只要：设置修改时间、修改人
		// 创建时间创建人没必要管他，因为我们是负责修改的
		category.setUpdateTime(LocalDateTime.now());
		category.setUpdateUser(BaseContext.getCurrentId());
		categoryMapper.update(category);
	}

	/**
	 * 启用、禁用分类
	 * @param status
	 * @param id
	 */
	@Override
	public void startOrStop(Integer status, Long id) {
		// 创建实体类Category对象
		Category category = Category.builder().status(status)
				.id(id).updateTime(LocalDateTime.now())
				.updateUser(BaseContext.getCurrentId())
				.build();

		// 将实体类对象传给mapper层（共用一个mapper层方法；）
		categoryMapper.update(category);
	}

	/**
	 * 根据类型查询分类
	 * @param type
	 * @return
	 */
	@Override
	public List<Category> list(Integer type) {
		List<Category> lists = categoryMapper.list(type);
		return lists;
	}
}
