package com.spy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-02-02 17:09
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
