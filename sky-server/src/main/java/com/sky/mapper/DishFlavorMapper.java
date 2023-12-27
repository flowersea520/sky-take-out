package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

/**
 * @author mortal
 * @date 2023/12/27 12:29
 */
@Mapper
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface DishFlavorMapper {
	/**
	 *  批量插入口味数据
	 * @param flavors
	 */
	 void insertBatch(List<DishFlavor> flavors);
}
