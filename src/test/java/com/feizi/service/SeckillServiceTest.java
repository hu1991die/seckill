package com.feizi.service;

import com.feizi.dto.Exposer;
import com.feizi.dto.SeckillExecution;
import com.feizi.entity.Seckill;
import com.feizi.exception.RepeatKillException;
import com.feizi.exception.SeckillClosedException;
import com.feizi.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Desc SeckillService业务接口测试用例
 * @Author feizi
 * @Date 2016/6/14 18:06
 * @Package_name com.feizi.service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
                        "classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> seckillList = this.seckillService.getSeckillList();
        logger.info("seckillList={}", seckillList);
    }

    @Test
    public void testGetById() throws Exception {
        long id = 1000;
        Seckill seckill = this.seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
        long id = 1001;
        Exposer exposer = this.seckillService.exportSeckillUrl(id);

        //exposer=Exposer{exposed=true, md5='f7fa6950f51227f8876378db4027d5ef', seckillId=1001, nowTime=0, startTime=0, endTime=0}
        logger.info("exposer={}",exposer);
    }

    /**
     * @Desc 测试完整逻辑,集成测试的覆盖完整性
     * @Author feizi
     * @Date 2016/6/15 18:06
     * @param
     * @return
     */
    @Test
    public void testExportSeckillLogic() throws Exception {
        long id = 1001;
        Exposer exposer = this.seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer={}",exposer);

            long userPhone = 18798789897L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = this.seckillService.executeSeckill(id, userPhone, md5);
                logger.info("seckillExecution={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }catch (SeckillClosedException e){
                logger.error(e.getMessage());
            }
        }else{
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void testExecuteSeckill() throws Exception {
        long id = 1001;
        long userPhone = 18798789896L;
        String md5 = "f7fa6950f51227f8876378db4027d5ef";

        try {
            SeckillExecution seckillExecution = this.seckillService.executeSeckill(id, userPhone, md5);
            //seckillExecution=SeckillExecution{seckillId=1001, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1001, user_phone=0, state=0, createTime=Wed Jun 15 17:49:26 CST 2016}}
            logger.info("seckillExecution={}",seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        }catch (SeckillClosedException e){
            logger.error(e.getMessage());
        }
    }

    /**
     * @Desc 测试调用存储过程执行秒杀操作
     * @Author feizi
     * @Date 2016/7/10 15:25
     * @param
     * @return
     */
    @Test
    public void testExecuteSeckillByProcedure() throws Exception{
        long id = 1004;
        long userPhone = 18565815837L;

        Exposer exposer = this.seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer={}",exposer);
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = this.seckillService.executeSeckill(id, userPhone, md5);
                logger.info(seckillExecution.getStateInfo());
                logger.info("seckillExecution={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }catch (SeckillClosedException e){
                logger.error(e.getMessage());
            }
        }else{
            logger.error(exposer.toString());
        }
    }
}