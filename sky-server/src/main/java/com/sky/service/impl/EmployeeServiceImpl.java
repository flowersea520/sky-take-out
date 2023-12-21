package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        /**
         * 在IntelliJ IDEA中，TODO是一种特殊的代码注释，用于标记代码中需要注意、尚未完成或需要进一步处理的部分。
         * TODO注释会以特殊的方式突出显示，开发者可以通过查看IDEA的TODO工具窗口，
         * 轻松地找到和管理项目中的所有TODO注释。这有助于团队更好地追踪和处理待办事项。
         */
        //密码比对
        // 对前端传入的密码password 进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误（和数据库中的密码对比）
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     *  新增员工方法
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        System.out.println("当前线程的id是：" + Thread.currentThread().getId());


        // 这里业务层要调用持久层mapper，所以要用到实体类entity；
        // 则，这里需要将employeeDTO转换成实体类entity
        // 这里使用对象属性拷贝快一些；
        Employee employee = new Employee();

        // 对象属性拷贝（第一个的参数是源对象；第二个参数：是要拷贝到的目标对象）
        // 拷贝的前提是：对应的属性名必须一致
        BeanUtils.copyProperties(employeeDTO, employee); // 这里将employeeDTO拷贝到employee中去

        // （未被拷贝的属性单独设置）
        // 设置账号的状态，默认正常状态 1 表示正常  0表示锁定
        employee.setStatus(StatusConstant.ENABLE); // 这里调用自定义的常量类，避免硬编码（写死状态）

        // 设置密码，默认密码123456（记得对他进行md5加密，调用工具类）
        // 这里也是调用一个常量类；避免硬编码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置当前的记录的创建时间和修改时间(LocalDateTime有时分秒）
        // LocalTime（只有年月日）
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());


        // 设置当前记录创建人id和修改人id（暂时写死）
        // 从当前ThreadLocal中，取出线程ID
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        // 调用数据层（记住传复制好属性的实体类对象）
        employeeMapper.insert(employee); // 最好不要用save方法，在数据层最好见名知意


    }
    /**
     *  员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // limit 起始索引,每页记录数;
        // 这里分页查询，直接用插件：PageHelper（页码，每页记录数）
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        // 这个page是pageHelper插件提供的，本质是arraylist集合
        Page<Employee> page =  employeeMapper.pageQuery(employeePageQueryDTO);
        // 在page对象中拿到total记录数和 records 员工的封装集合对象
        long total = page.getTotal();
        List<Employee> records = page.getResult();

        // 将total和records封装到PageResult对象中去
        return new PageResult(total, records);
    }

}
