package com.spy.dto;

import com.spy.entity.OrderDetail;
import com.spy.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author spy
 * @create 2023-03-13 16:02
 */
@Data
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
