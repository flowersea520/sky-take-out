package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
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

	/**
	 * 批量删除套餐
	 * @param ids
	 */
	// 像这种同时进行的，要用到事务
	@Transactional
	@Override
	public void deleteBatch(List<Long> ids) {
		// 遍历传递过来的集合，将集合中的每个id，都调用对应的套餐mapper查询出对应的套餐对象
		ids.forEach(id -> {
			Setmeal setmeal =  setmealMapper.getById(id);
			// 做一个判断 如果当前套餐在起售中，则 不能删除
			if (StatusConstant.ENABLE == setmeal.getStatus()) {
				// 抛出异常给前端
				throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
			}
		});

		// 刚开始判断完在起售状态的套餐id后，现在的id，都是未起售的，所以可以再次遍历了
		ids.forEach(setMealId -> {
			// 直接根据 id删除对应的表中的数据
			setmealMapper.deleteById(setMealId);
			// 删除套餐菜品中关联表的数据
			setmealDishMapper.deleteBySetmealId(setMealId);
		});
	}

	/**
	 *   根据id查询套餐和套餐关联的菜品数据，用于修改页面回显数据
	 * @param id
	 * @return
	 */
	@Override
	public SetmealVO getByIdWithDish(Long id) {
			// 根据id查询套餐和套餐关联的菜品数据，用于修改页面回显数据
		Setmeal setmeal = setmealMapper.getById(id);
		// 根据这个id查询套餐关联的菜品数据（返回的是一个数组
		List<SetmealDish> setmealDishes =  setmealDishMapper.getBySetmealId(id);
		// 创建一个SetmealVo对象，然后让 将setmeal属性拷贝到Vo中去
		SetmealVO setmealVO = new SetmealVO();
		BeanUtils.copyProperties(setmeal, setmealVO);
		// setmealDishes单独设置
		setmealVO.setSetmealDishes(setmealDishes);



		return setmealVO;
	}

	/**
	 * 修改套餐（这里也要修改第三张表，所以要用事件注解）
	 * @param setmealDTO
	 */
	@Transactional
	@Override
	public void update(SetmealDTO setmealDTO) {
		// 创建一个套餐对象，将DTO的属性，赋值到 setmeal实体类上
		// 因为前端传过来修改的DTO不够全面，我们要修改全面的数据库表entity
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(setmealDTO, setmeal);

		// 修改套餐表i，执行update
		setmealMapper.update(setmeal);

		// 拿到要修改的套餐id
		Long setmealId = setmealDTO.getId();
		// 2. 删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
		// 就是根据id删除套餐关联的菜品表（第三张表）
		setmealDishMapper.deleteBySetmealId(setmealId);

		// 这行代码从一个setmealDTO对象中获取了一个SetmealDish对象的列表，
		List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
		/**
		 * 这部分代码遍历了setmealDishes列表中的每一个setmealDish对象，并调用其setSetmealId方法，
		 * 将setmealId的值设置给每一个setmealDish对象。
		 */
		setmealDishes.forEach(setmealDish -> {
			// 我们在前端点击添加菜品按钮时候：里面是没有设置关联的套餐id的，所以要单独设置
			setmealDish.setSetmealId(setmealId);
		});

		// 3. 重新插入（修改好的）套餐和菜品的关联关系，操作setmeal_dish表，执行insert
		setmealDishMapper.insertBatch(setmealDishes);



	}

	/**
	 *  起售停售套餐
	 * @param status
	 * @param id
	 */
	@Override
	public void stopOrStop(Integer status, Long id) {
		// 起售套餐时，判断套餐内是否有停售菜品，如果有停售菜品则提示“套餐内包含未启售菜品，无法起售”
		if(status == StatusConstant.ENABLE){ // 在起售条件里面找
			// 起售，根据套餐id获取对应的 菜品列表（用到多表查询了）
			// //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
			List<Dish> dishList =  dishMapper.getBySetmealId(id);
			if (dishList != null && dishList.size() >0) {
				// 判断里面有菜品
				// 遍历菜品列表里面的菜品，判断其内部是否有停售菜品，如果有，抛出异常给前端（并提示：“套餐内包含未启售菜品，无法起售”）
				dishList.forEach(dish -> {
					if (dish.getStatus() == StatusConstant.DISABLE) {
						throw new  SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
					}
				});

			}
		}
	}
}
