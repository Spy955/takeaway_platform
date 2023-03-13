package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.common.BaseContext;
import com.spy.entity.ShoppingCart;
import com.spy.mapper.ShoppingCartMapper;
import com.spy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-13 10:48
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        this.remove(queryWrapper);
    }
}
