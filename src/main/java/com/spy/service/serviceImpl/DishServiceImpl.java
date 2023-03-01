package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.Dish;
import com.spy.mapper.DishMapper;
import com.spy.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-01 18:01
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
