package com.feizi.dao.cache;

import com.feizi.dao.SeckillDao;
import com.feizi.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @Desc
 * @Author feizi
 * @Date 2016/7/9 15:29
 * @Package_name com.feizi.dao.cache
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest{

    private long id = 1001;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() throws Exception {
        //get and set
       Seckill seckill = redisDao.getSeckill(id);
        if(null == seckill){
            seckill = seckillDao.queryById(id);
            if(null != seckill){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);

                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }
}