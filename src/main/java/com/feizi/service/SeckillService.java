/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.service;

import com.feizi.dto.Exposer;
import com.feizi.dto.SeckillExecution;
import com.feizi.entity.Seckill;
import com.feizi.exception.RepeatKillException;
import com.feizi.exception.SeckillClosedException;
import com.feizi.exception.SeckillException;

import java.util.List;

/**
 * @Desc 业务接口：站在使用者的角度去设计接口，而不是实现
 * 三个方面：
 * 1、方法定义粒度，方法定义非常明确
 * 2、参数，参数尽量简便
 * 3、返回类型（return 类型/异常）
 * @Author feizi
 * @Date 2016/6/8 15:50
 * @Package_name com.feizi.service
 */
public interface SeckillService {

    /**
     * @Desc 查询所有秒杀记录
     * @Author feizi
     * @Date 2016/6/8 15:54
     * @param
     */
    List<Seckill> getSeckillList();

    /**
     * @Desc 查询单个秒杀记录
     * @Author feizi
     * @Date 2016/6/8 16:00
     * @param
     */
    Seckill getById(long seckillId);

    /**
     * @Desc 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @Author feizi
     * @Date 2016/6/8 16:01
     * @param
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * @Desc 执行秒杀操作
     * @Author feizi
     * @Date 2016/6/8 16:12
     * @param
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillClosedException;
}