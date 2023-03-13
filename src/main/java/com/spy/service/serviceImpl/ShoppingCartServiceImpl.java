package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
