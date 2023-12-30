package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
	@Autowired
	private SetmealDishMapper setmealDishMapper;


	/**
	 * 新增菜品
	 *
	 * @param dishDTO 使用@Transactional注解，可以更加方便地管理事务，保障数据的一致性和可靠性。
	 *                一致性：表示事务执行前后数据库的状态保持一致。
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

	/**
	 * 菜品分页查询
	 *
	 * @param dishPageQueryDTO
	 * @return
	 */
	@Override
	public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
		// pageNum – 表示要查询的页码 pageSize – 表示每页显示的记录数。
		PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
		Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
		// 在分页功能中，`records`**代表当前这一页要展示的数据集合,数据多个,为数组 (当前页面的对象集合）
		// 注意前端的records数组对象，等于分页插件中的：page.getResult()*
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 批量删除菜品
	 *
	 * @param ids
	 * @Transactional在业务层进行了多表操作， 记得要设置事务注解，保证数据一致性（这个三个判断1，2，3）要么同时成功，要么同时失败；；
	 */
	@Transactional
	@Override
	public void deleteBatch(List<Long> ids) {
		// 1. 判断当前菜品是否能够删除 -- 是否存在起售中的菜品 （每一种情况对应着一个mapper方法）
		for (Long id : ids) {
			// 调用dish的mapper层，是否能够查到id对应的菜品对象；
			Dish dish = dishMapper.getById(id);
			if (dish.getStatus() == StatusConstant.ENABLE) {//这里的状态肯定是固定了，所以定义了常量
				// 当前菜品处于起售中，不能删除（调用定义好的异常类）
				throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE); // 异常类中的参数，就是报错信息
			}
		}

		// 2. 判断当前菜品是否能够删除 -- 是否被套餐关联了 -- （每一种情况对应着一个mapper方法）
		List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
		if (setmealIds != null && setmealIds.size() > 0) {
			// 满足上面的判断条件，说明当前菜品关联了套餐
			// 不能删除，抛出异常
			throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

		}


//		// 删除菜品表中菜品数据
//		for (Long id : ids) {
//			dishMapper.deleteById(id);
//			// 删除菜品关联的口味数据；
//			dishFlavorMapper.deleteByDishId(id);
//		}

		// 在for循环中 随着ids集合的元素越多，它sql执行的次数就越多；所以进行代码优化
		// sql: delete from dish where id in(?,?,?)
		// 根据菜品id集合批量删除菜品数据
		dishMapper.deleteByIds(ids);

		// 根据菜品Id集合批量删除关联口味数据
		// sql: delete from dish_flavor where dish_id in(?,?,?)
		dishFlavorMapper.deleteByDishIds(ids);


	}

	/**
	 * 根据id查询菜品和对应的口味数据
	 *
	 * @param id
	 * @return
	 */
	@Override
	public DishVO getByIdWithFlavor(Long id) {
		// 根据id查询菜品数据
		Dish dish = dishMapper.getById(id);

		// 根据菜品id查询口味数据  (会查到多种口味，可以看数据库）
		List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

		// 将查询到的数据封装到dishVO中
		DishVO dishVO = new DishVO();
		// 进行对象拷贝，将dish，拷贝到dishVo中去；
		BeanUtils.copyProperties(dish, dishVO); // VO中的categoryName用不到，所以让他为null
		// 将没有拷贝到的属性单独设置（少了flavors集合属性）
		dishVO.setFlavors(dishFlavors); // 将dishFlavors口味集合封装到VO中


		return dishVO;
	}

	/**
	 * 修改菜品基本信息和对应口味信息
	 *
	 * @param dishDTO
	 * @return
	 */
	@Override
	public void updateWithFlavor(DishDTO dishDTO) { // 前端传过来修改的新数据都在dishDTO里面了
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);

		// 修改菜品表的基本信息：（不包括flavor口味属性），所以用dish对象
		dishMapper.update(dish);
		// 这里还剩flavor口味属性没有设置修改更新
		// 删除原有的口味数据 （根据前端的DTO的菜品id删除）
		dishFlavorMapper.deleteByDishId(dishDTO.getId());

		// 重新插入新的口味数据
		List<DishFlavor> flavors = dishDTO.getFlavors(); // 拿到前端DTO的口味集合属性

		// 判断是否该口味flavors集合是否为空
		if (flavors != null && flavors.size() > 0) {
			// 集合不为空，则遍历它，对每个flavor对象进行菜品id赋值
			flavors.forEach(dishFlavor -> {
				dishFlavor.setDishId(dishDTO.getId());
			});
			// 向口味表插入n条数据 (前面 修改的操作 没有动用dish_flavor表，现在单独设置）
			dishFlavorMapper.insertBatch(flavors);
		}


	}

}

