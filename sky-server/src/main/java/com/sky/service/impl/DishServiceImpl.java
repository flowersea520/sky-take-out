package com.sky.service.impl;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/27 11:23
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private DishFlavorMapper dishFlavorMapper;

	/**
	 *  新增菜品
	 * @param dishDTO
	 * 使用@Transactional注解，可以更加方便地管理事务，保障数据的一致性和可靠性。
	 * 一致性：表示事务执行前后数据库的状态保持一致。
	 */
	@Transactional // 使用了
	@Override
	public void saveWithFlavor(DishDTO dishDTO) {
		// 有DTO就有entity，对象拷贝
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		// TODO 查看一下是否生效那个使用AOP注解拦截，为公共字段赋值；
		dishMapper.insert(dish);

		// 获取insert语句生成的主键值；（在xml中设置了）
		Long dishId = dish.getId();


		// 获取dishDTO中的口味属性集合对象
		List<DishFlavor> flavors = dishDTO.getFlavors();
		// 如果该口味flavors集合，不为空，且元素个数大于0，就执行；
		if (flavors != null && flavors.size() > 0) {
			// 遍历这个flavors集合
			// 遍历出来的每个flavor元素，进行id赋值
			flavors.forEach(dishFlavor -> { //dishFlavor 是Lambda表达式的参数，代表集合中的每一个元素。
				dishFlavor.setDishId(dishId);
			});



			// 向口味表插入n条数据（所以要用到口味表对应的mapper）
			// batch：分批处理  的意思
			dishFlavorMapper.insertBatch(flavors);


		}
	}
}
