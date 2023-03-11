package com.spy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.dto.SetmealDto;
import com.spy.entity.Setmeal;

import java.util.List;

/**
 * @author spy
 * @create 2023-03-01 17:59
 */
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDishById(SetmealDto setmealDto);

    void deleteWithDishById(List<Long> ids);

}
