package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-02-28 9:42
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
