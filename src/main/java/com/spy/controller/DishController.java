package com.spy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spy.common.R;
import com.spy.dto.DishDto;
import com.spy.entity.Category;
import com.spy.entity.Dish;
import com.spy.entity.DishFlavor;
import com.spy.service.CategoryService;
import com.spy.service.DishFlavorService;
import com.spy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 菜品信息分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    /*
    //不显示菜品分类，因此要引入dishDto来进行显示
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort);
        dishService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
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
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
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
        //由于菜品更新所以需要删除Redis中菜品的相关信息
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
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
            String key = "dish_" + item.getCategoryId() + "_1";
            item.setStatus(st);
            redisTemplate.delete(key);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishList);
        return R.success("菜品状态修改成功");
    }


    /**
     * 套餐新增时将菜品分类中的各菜品信息进行回显
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //使用Redis缓存数据提升系统性能
        List<DishDto> dishDtoList = null;
        //构造唯一的key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //先从Redis中查找是否存在
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //判断是否存在
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }
        //构造查询条件对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（启售）
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            //把dish的信息拷贝到dishDto中
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //查询对应的口味信息
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(flavors);

            return dishDto;
        }).collect(Collectors.toList());

        //查询到之后进行保存到Redis操作
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }


}
