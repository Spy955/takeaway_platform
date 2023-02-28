package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.Employee;
import com.spy.mapper.EmployeeMapper;
import com.spy.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-02-28 9:41
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    
}
