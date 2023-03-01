package com.spy.common;

/**
 * @author spy
 * @create 2023-03-01 18:12
 * 自定义业务异常类
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
