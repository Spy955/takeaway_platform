package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.Setmeal;
import com.spy.mapper.SetmealMapper;
import com.spy.service.SetmealService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-01 18:00
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
