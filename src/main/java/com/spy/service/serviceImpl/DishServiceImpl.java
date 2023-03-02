package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.common.CustomException;
import com.spy.dto.DishDto;
import com.spy.entity.Dish;
import com.spy.entity.DishFlavor;
import com.spy.mapper.DishMapper;
import com.spy.service.DishFlavorService;
import com.spy.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author spy
 * @create 2023-03-01 18:01
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 保存菜品信息以及菜品口味信息
     * @param dishDto
     */
    @Transactional
    public void dishSaveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 通过id查找菜品及口味信息用于回显
     * @param id
     */
    @Override
    public DishDto getByIdWthFlavor(Long id) {
        DishDto dishDto = new DishDto();
        //首先获取菜品的信息
        Dish dish = this.getById(id);
        //将菜品信息复制到DishDto中
        BeanUtils.copyProperties(dish,dishDto);
        //获取菜品的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //将菜品口味信息设置给dishDto
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品及其口味的信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void dishUpdateWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.updateById(dishDto);

        //删除菜品之前的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }


    /**
     * 删除停止售卖的菜品及其口味
     * @param ids
     */
    @Override
    @Transactional
    public void dishRemoveWithFlavor(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("当前菜品正在售卖中，请勿删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(lambdaQueryWrapper);
    }



}
