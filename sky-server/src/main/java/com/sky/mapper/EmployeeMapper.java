package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Mapper
//Spring会根据配置的切入点表达式和通知方法，自动为mapper层的方法创建代理对象，从而实现AOP的拦截和通知逻辑。
@EnableAspectJAutoProxy(proxyTargetClass = true)
public interface EmployeeMapper {

	/**
	 * 根据用户名查询员工
	 *
	 * @param username
	 * @return
	 */
	@Select("select * from employee where username = #{username}")
	Employee getByUsername(String username);

	/**
	 * 插入员工数据（由于sql简单，就不用写在xml中了
	 * id让其自增，其他的手动插入
	 * @param employee
	 */
	@Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status) " +
			"values " +
			"(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
	void insert(Employee employee);

	/**
	 *  员工分页查询
	 * @param employeePageQueryDTO
	 * @return
	 */
	Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


	/**
	 * 启用禁用员工账号
	 *  根据主键，动态修改属性
	 * @param employee
	 */
	void update(Employee employee);

	/**
	 * 根据id查询员工
	 * @param id
	 * @return
	 */
	@Select("select id, name, username, password," +
			" phone, sex, id_number, status, create_time, update_time, create_user, update_user from employee where id = #{id}")
	Employee getById(Long id);

	/**
	 * 编辑员工信息
	 */
	void updateEmpMeg(Employee employee);
}
