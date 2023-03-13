package com.spy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.entity.Orders;

/**
 * @author spy
 * @create 2023-02-02 17:10
 */
public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
