package com.spy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.entity.Category;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-01 16:50
 */
public interface CategoryService extends IService<Category> {

    //定义删除没有关联的方法
    void remove(Long id);
}
