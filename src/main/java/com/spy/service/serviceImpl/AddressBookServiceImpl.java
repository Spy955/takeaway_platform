package com.spy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.entity.AddressBook;
import com.spy.mapper.AddressBookMapper;
import com.spy.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author spy
 * @create 2023-03-13 10:52
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
