package com.spy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author spy
 * @create 2023-03-13 10:51
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
