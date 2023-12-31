package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
// 这个注解knife4j的注解，用在Controller类上的，
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    // 这个注解knife4j的注解，用在方法上的，
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    // 你个属性value可不写
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工方法
     * @param employeeDTO
     * @return
     */

    @PostMapping
    @ApiOperation("新增员工") // 对方法进行描述的 文档生成（定义在方法上）
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        // 输入调用日志信息slf4j；代表这个方法被执行了
        log.info("新增员工：{}", employeeDTO);

//        System.out.println("当前线程的id是：" + Thread.currentThread().getId());

        // controller层调用业务层service
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     *  员工分页查询
     * @param employeePageQueryDTO 分页查询的实体类
     * @return
     */
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    // **请求参数类型为Query，**不是json格式提交，**Query在路径后直接拼接 ?参数
    // 只要不是JSON，就没必要加@RequestBody注解
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO); // 查询返回员工PageResult对象
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号（这里是更新status状态码的操作）
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status, Long id) { // 使用@PathVariable注解，接收路径中的参数‘
        log.info("启用禁用员工账号，{}，{}", status, id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息（前端点击”修改“按钮，会出现数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getById (@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    // 除了查询操作一般其他的都不用泛型，因为不要返回特定的数据
    public Result updateEmpMeg(@RequestBody EmployeeDTO employeeDTO) {// 传过来的是josn格式，且参数不多，用定义好的DTO
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.updateEmpMeg(employeeDTO);
        return Result.success();
    }


}
