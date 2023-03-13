package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-03-13 10:46
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
