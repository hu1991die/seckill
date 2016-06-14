package com.feizi.dao;

import com.feizi.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Desc
 * @Author feizi
 * @Date 2016/6/8 14:41
 * @Package_name com.feizi.dao
 */
//配置Spring和Junit整合,junit启动时加载SpringIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {
        int rows = successKilledDao.insertSuccessKilled(1002L, 18565815836L);
        System.out.println("rows=" + rows);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1002L, 18565815836L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}