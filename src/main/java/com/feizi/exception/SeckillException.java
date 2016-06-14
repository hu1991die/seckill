/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.exception;

/**
 * @Desc 秒杀相关业务异常
 * @Author feizi
 * @Date 2016/6/8 16:35
 * @Package_name com.feizi.exception
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
