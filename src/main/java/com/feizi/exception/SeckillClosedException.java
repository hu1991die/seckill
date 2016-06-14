/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.exception;

/**
 * @Desc 秒杀关闭异常（库存完了，时间到了）
 * @Author feizi
 * @Date 2016/6/8 16:33
 * @Package_name com.feizi.exception
 */
public class SeckillClosedException extends SeckillException{

    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
