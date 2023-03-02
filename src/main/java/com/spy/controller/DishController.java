package com.spy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spy.common.R;
import com.spy.dto.DishDto;
import com.spy.entity.Category;
import com.spy.entity.Dish;
import com.spy.service.CategoryService;
import com.spy.service.DishFlavorService;
import com.spy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author spy
 * @create 2023-03-01 21:32
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品信息分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort);
        dishService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 添加菜品功能实现
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDTO:{}",dishDto.toString());
        dishService.dishSaveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    /**
     * 用于菜品信息回显
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWthFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 用于菜品修改
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.dishUpdateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 菜品及口味删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.dishRemoveWithFlavor(ids);
        return R.success("菜品删除成功");
    }

    /**
     * 菜品批量起售停售
     *
     * @param st
     * @param ids
     * @return
     */
    @PostMapping("/status/{st}")
    public R<String> updateStatus(@PathVariable Integer st, @RequestParam List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        List<Dish> dishList = dishService.list(queryWrapper);
        dishList = dishList.stream().map((item) -> {
            item.setStatus(st);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishList);
        return R.success("菜品起售停售修改成功");
    }


}
