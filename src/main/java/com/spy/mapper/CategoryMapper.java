package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-03-01 16:49
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
