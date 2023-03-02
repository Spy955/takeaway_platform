package com.spy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.dto.DishDto;
import com.spy.entity.Dish;

import java.util.List;

/**
 * @author spy
 * @create 2023-03-01 17:58
 */
public interface DishService extends IService<Dish> {
    void dishSaveWithFlavor(DishDto dishDto);

    DishDto getByIdWthFlavor(Long id);

    void dishUpdateWithFlavor(DishDto dishDto);

    void dishRemoveWithFlavor(List<Long>ids);


}
