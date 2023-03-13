package com.spy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.entity.ShoppingCart;

/**
 * @author spy
 * @create 2023-03-13 10:47
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}
