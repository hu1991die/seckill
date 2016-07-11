/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.service.impl;

import com.feizi.constants.SeckillStateConstantEnum;
import com.feizi.dao.SeckillDao;
import com.feizi.dao.SuccessKilledDao;
import com.feizi.dao.cache.RedisDao;
import com.feizi.dto.Exposer;
import com.feizi.dto.SeckillExecution;
import com.feizi.entity.Seckill;
import com.feizi.entity.SuccessKilled;
import com.feizi.exception.RepeatKillException;
import com.feizi.exception.SeckillClosedException;
import com.feizi.exception.SeckillException;
import com.feizi.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc
 * @Author feizi
 * @Date 2016/6/8 17:00
 * @Package_name com.feizi.service.impl
 * @Component所有的组件（不区分是Dao还是Service或者是Controller）注解
 * 如果已经知道了是Dao,则直接写@Resource
 * 如果已经知道了是Service，则直接写@Service
 * 如果已经知道了是Controller,则直接写@Controller
 */
@Service
public class SeckillServiceImpl implements SeckillService{

    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /***
     * 注入Service依赖,三种方式：
     * @Autowired(Spring一直采用的方式：自动注入)
     * @Resource
     * @Inject
     */
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //加入盐值,md5盐值字符串，用于混淆MD5
    private final String salt = "1234567890)(*&^%$#@!";


    /**
     * @Desc 查询所有秒杀记录
     * @Author feizi
     * @Date 2016/6/8 15:54
     */
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    /**
     * @param seckillId
     * @Desc 查询单个秒杀记录
     * @Author feizi
     * @Date 2016/6/8 16:00
     */
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * @param seckillId
     * @Desc 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @Author feizi
     * @Date 2016/6/8 16:01
     */
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：redis缓存优化,超时的基础上维护一致性
        /***
         * get from cache
         * if null
         *  get db
         *  put cache
         * else
         *  logic
         */
        //1、访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(null == seckill){
            //2、访问数据库
            seckill = getById(seckillId);
            if(null == seckill){
                //当前无秒杀库存商品
                return new Exposer(false, seckillId);
            }else{
                //3、放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();

        if(nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()){
            //当前时间不在秒杀时间范围内
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //TODO 转化特定字符串的过程,不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * @param seckillId
     * @param userPhone
     * @param md5
     * @Desc 执行秒杀操作
     * @Author feizi
     * @Date 2016/6/8 16:12
     * 使用注解控制事务方法的优点：
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2: 保证事务方法的执行时间尽可能短,不要穿插其他网络操作 RPC/HTTP请求或者剥离到事务方法外部
     * 3：不是所有的方法操作都需要事务控制,如只有一条修改操作，只读操作不需要事务控制
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillClosedException {
        if(null == md5 || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite.");
            //自定义异常，返回到客户端
//            return new SeckillExecution(seckillId, SeckillStateConstantEnum.DATE_REWRITE);
        }

        //执行秒杀操作：减库存，记录秒杀操作
        Date killTime = new Date();
        try {
            //记录秒杀行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一验证:seckillId + userPhone
            if(insertCount <= 0){
                //重复秒杀
                throw new SeckillException("repeat kill........" );
//                return new SeckillExecution(seckillId,SeckillStateConstantEnum.REPEAT_KILL);
            }else{
                //减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, killTime);
                if(updateCount <= 0){
                    //没有更新到记录，秒杀结束
                    throw new SeckillException("seckill has ended........" );
//                    return new SeckillExecution(seckillId, SeckillStateConstantEnum.END);
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId ,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateConstantEnum.SUCCESS, successKilled);
                }
            }
        }catch (SeckillClosedException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
//            return new SeckillExecution(seckillId,SeckillStateConstantEnum.INNER_ERROR);
        }
    }

    /**
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @Desc 执行秒杀操作通过存储过程
     * @Author feizi
     * @Date 2016/7/10 14:13
     */
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillClosedException {
        if(null == md5 || !md5.equals(getMD5(seckillId))){
            //数据被篡改了
            return new SeckillExecution(seckillId, SeckillStateConstantEnum.DATE_REWRITE);
        }

        Date killTime = new Date();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("seckillId", seckillId);
        paramMap.put("phone", userPhone);
        paramMap.put("killTime", killTime);
        paramMap.put("result", null);

        //执行完存储过程之后，result被赋值
        try {
            seckillDao.executeSeckillByProcedure(paramMap);
            //获取result值
            int result = MapUtils.getInteger(paramMap, "result", -2);
            if(result == 1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateConstantEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId, SeckillStateConstantEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateConstantEnum.INNER_ERROR);
        }
    }

    /**
     * @Desc 获取MD5加密字符串
     * @Author feizi
     * @Date 2016/6/8 17:42
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
        String baseStr = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(baseStr.getBytes());
        return md5;
    }
}
