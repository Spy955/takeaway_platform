package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.User;
import com.spy.mapper.UserMapper;
import com.spy.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-11 10:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService   {
}
