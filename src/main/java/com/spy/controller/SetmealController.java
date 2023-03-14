package com.spy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spy.common.R;
import com.spy.dto.DishDto;
import com.spy.dto.SetmealDto;
import com.spy.entity.Category;
import com.spy.entity.Dish;
import com.spy.entity.Setmeal;
import com.spy.entity.SetmealDish;
import com.spy.service.CategoryService;
import com.spy.service.DishService;
import com.spy.service.SetmealDishService;
import com.spy.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author spy
 * @create 2023-03-02 13:22
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 套餐分页查询,操作两张表，要同时查出套餐分类名称
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝，由于泛型不一致，因此不需要拷贝records
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> dtoRecords = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            //使用分类id来查询分类名称
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtoRecords);
        return R.success(dtoPage);
    }


    /**
     * 新增套餐功能
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        String key = "setmeal_" + setmealDto.getCategoryId();
        redisTemplate.delete(key);
        return R.success("新增套餐成功");
    }

    /**
     * 进行修改菜品信息的回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDish = setmealService.getByIdWithDish(id);
        return R.success(setmealDish);
    }


    /**
     * 根据套餐id修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateById(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishById(setmealDto);
        String key = "setmeal_" + setmealDto.getCategoryId();
        redisTemplate.delete(key);
        return R.success("修改成功");
    }


    /**
     * 删除套餐及其对应的菜品功能
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDishById(ids);
        return R.success("删除套餐成功");
    }


    /**
     * 批量起售停售操作
     * @param st
     * @param ids
     * @return
     */
    @PostMapping("/status/{st}")
    public R<String> updateStatus(@PathVariable Integer st, @RequestParam List<Long> ids){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        setmealList.stream().map((item) -> {
            String key = "setmeal_" + item.getCategoryId();
            redisTemplate.delete(key);
            item.setStatus(st);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmealList);
        return R.success("套餐状态修改成功");
    }


    /**
     * 用于客户端套餐展示
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        List<Setmeal> setmealList = null;
        String key = "setmeal_" + setmeal.getCategoryId();
        setmealList = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if (setmealList != null) {
            return R.success(setmealList);
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,1);
        setmealList = setmealService.list(queryWrapper);

        redisTemplate.opsForValue().set(key,setmealList,60, TimeUnit.MINUTES);
        return R.success(setmealList);
    }

    /**
     * 客户端点击套餐时，显示套餐的详细信息
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> setmealDesc(@PathVariable Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        List<DishDto> dishDtos = setmealDishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish,dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }


}
