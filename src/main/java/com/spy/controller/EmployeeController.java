package com.spy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spy.common.R;
import com.spy.entity.Employee;
import com.spy.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author spy
 * @create 2023-02-28 9:34
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.首先判断数据库是否存在用户名
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(queryWrapper);
        //2.判断是否存在用户
        if (emp == null) {
            return R.error("该用户不存在");
        }
        //3.对用户密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //4.判断密码是否一致，不一致则报错
        if (!emp.getPassword().equals(password)){
            return R.error("账号或密码错误");
        }
        //5.判断员工状态
        if (emp.getStatus() == 0) {
            return R.error("员工账号被禁用");
        }
        //6.登陆成功，将员工id存到session域
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 登出功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("登出成功");
    }

    /**
     * 新增员工功能
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        //1.设置初始密码123456，但是要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //2.保存用户
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页数据查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序条件
        queryWrapper.orderByAsc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 用于修改员工信息时的信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return R.error("未找到员工信息");
        }
        return R.success(employee);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }



}
