package com.spy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spy.common.BaseContext;
import com.spy.common.R;
import com.spy.entity.Setmeal;
import com.spy.entity.ShoppingCart;
import com.spy.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author spy
 * @create 2023-03-13 10:49
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加菜品或套餐操作
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置id，表示购物车的主人
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //判断当前加入到购物车的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if (dishId == null) {
            //表明加入的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        } else {
            //加入的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }

        //查看当前菜品或者套餐是否在购物车之中
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if (cart == null) {
            //表明菜品是第一次加入购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        } else {
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        }

        return R.success(cart);
    }


    /**
     * 展示购物车数据
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        shoppingCartService.clean();
        return R.success("购物车清空成功");
    }







}
