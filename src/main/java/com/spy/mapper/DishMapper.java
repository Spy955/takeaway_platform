package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-03-01 17:58
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
