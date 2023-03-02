package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.DishFlavor;
import com.spy.mapper.DishFlavorMapper;
import com.spy.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-01 21:31
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
