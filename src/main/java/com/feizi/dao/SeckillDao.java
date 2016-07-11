/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.dao;

import com.feizi.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Desc 秒杀库存数据库访问dao层
 * @Author feizi
 * @Date 2016/6/4 19:23
 * @Package_name com.feizi.dao
 */
public interface SeckillDao {

    /**
     * @Desc 减库存
     * @Author feizi
     * @Date 2016/6/4 19:25
     * @param seckillId 秒杀商品id
     * @param killTime 秒杀时间
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * @Desc 根据秒杀库存id查询秒杀商品对象
     * @Author feizi
     * @Date 2016/6/4 19:27
     * @param 
     */
    Seckill queryById(long seckillId);

    /**
     * @Desc 分页查询所有秒杀商品列表
     * @Author feizi
     * @Date 2016/6/4 19:28
     * @param 
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Desc 使用存储过程执行秒杀
     * @Author feizi
     * @Date 2016/7/10 14:45
     * @param
     * @return
     */
    void executeSeckillByProcedure(Map<String,Object> paramMap);
}