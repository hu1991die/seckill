/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.dao;

import com.feizi.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @Desc 秒杀成功明细记录数据库访问Dao层
 * @Author feizi
 * @Date 2016/6/4 19:29
 * @Package_name com.feizi.dao
 */
public interface SuccessKilledDao {

    /**
     * @Desc 插入秒杀成功明细记录,可过滤重复
     * 插入的行数
     * @Author feizi
     * @Date 2016/6/4 19:32
     * @param
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * @Desc 根据id查询SuccessKilled并且携带秒杀商品对象实体
     * @Author feizi
     * @Date 2016/6/4 19:33
     * @param
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}