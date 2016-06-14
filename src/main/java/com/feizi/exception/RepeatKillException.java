/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.exception;

/**
 * @Desc 重复秒杀异常（运行期异常）
 * Java异常主要分为编译期异常和运行期异常，运行期异常不需要手动try-catch,Spring
 * 的声明式事务只接受运行期异常回滚策略，当程序抛出非运行期异常时Spring不会帮我们
 * 做回滚
 * @Author feizi
 * @Date 2016/6/8 16:23
 * @Package_name com.feizi.exception
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
