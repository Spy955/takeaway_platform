package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.common.CustomException;
import com.spy.entity.Category;
import com.spy.entity.Dish;
import com.spy.entity.Setmeal;
import com.spy.mapper.CategoryMapper;
import com.spy.service.CategoryService;
import com.spy.service.DishService;
import com.spy.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-01 16:51
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Override
    public void remove(Long id) {
        //判断分类下面是否关联了菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前套餐下关联了菜品，无法删除");
        }
        //判断分类下面是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前套餐下关联了菜品，无法删除");
        }
        super.removeById(id);
    }
}
