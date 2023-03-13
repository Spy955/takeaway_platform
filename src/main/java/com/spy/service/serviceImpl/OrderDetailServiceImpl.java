package com.spy.service.serviceImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.OrderDetail;
import com.spy.mapper.OrderDetailMappper;
import com.spy.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-02-02 17:13
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMappper, OrderDetail> implements OrderDetailService {
}
