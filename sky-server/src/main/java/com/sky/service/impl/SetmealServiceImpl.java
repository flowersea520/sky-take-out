package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mortal
 * @date 2024/1/6 17:06
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
	@Autowired
	private SetmealMapper setmealMapper;
	@Autowired
	private SetmealDishMapper setmealDishMapper;
	@Autowired
	private DishMapper dishMapper;


	/**
	 * 新增套餐，同时需要保存套餐和菜品的关联关系；
	 * 为了保持代码的一致性，记得加上@Transactional注解
	 * @param setmealDTO
	 */
	@Transactional
	@Override
	public void saveWithDish(SetmealDTO setmealDTO) {
		// 在DTO一定要在业务层进行对象copy，转换成对应的entity
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);
		// 所有的属性都复制上了，entity没有剩下的属性了

		// 向套餐表插入数据
		setmealMapper.insert(setmeal);

		// 获取自动生成的id
		Long setmealId = setmeal.getId();

		// 获取DTO中的setmealDishes集合对象（套餐和菜品关联的对象）
		List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
		// 遍历该集合对象，给其的setmealId套餐id赋值
		for (SetmealDish setmealDish : setmealDishes) {
			setmealDish.setSetmealId(setmealId);
		}
		// 保存套餐和菜品的关联关系
		// setmealDishes是前端传过来的，在DTO中，所以要保存到对应的关联表中去
		setmealDishMapper.insertBatch(setmealDishes);

	}

	/**
	 * 套餐分页查询
	 * @param setmealPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
		// 拿到前端传过来的页码和每页记录数
		int pageNum = setmealPageQueryDTO.getPage(); // 页码
		int pageSize = setmealPageQueryDTO.getPageSize(); // 每页记录数

		// 使用分页插件，开始分页
		// 当你调用这个方法后，后续的查询会自动应用分页逻辑，返回特定页的数据。
		PageHelper.startPage(pageNum,pageSize);

		// 调用mapper层（传入的肯定是实体类，这里用DTO三个参数，够了：可以根据需要，按照套餐名称、分类、售卖状态进行查询）
		Page<SetmealVO> page =  setmealMapper.pageQuery(setmealPageQueryDTO);

		return new PageResult(page.getTotal(), page.getResult()); // 这里对应接口文档的data
	}
}
