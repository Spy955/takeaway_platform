package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-03-11 10:11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
