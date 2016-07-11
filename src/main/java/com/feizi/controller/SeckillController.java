/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.controller;

import com.feizi.constants.SeckillStateConstantEnum;
import com.feizi.dto.Exposer;
import com.feizi.dto.SeckillExecution;
import com.feizi.dto.SeckillResult;
import com.feizi.entity.Seckill;
import com.feizi.exception.RepeatKillException;
import com.feizi.exception.SeckillClosedException;
import com.feizi.exception.SeckillException;
import com.feizi.service.SeckillService;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Desc
 * @Author feizi
 * @Date 2016/6/20 17:06
 * @Package_name com.feizi.controller
 */
@Controller
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分 /seckill/list
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * @param
     * @return
     * @Desc 秒杀列表页接口
     * @Author feizi
     * @Date 2016/6/20 17:20
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);

        //list.jsp + model = ModelAndView
        return "list";// /WEB-INF/jsp/list.jsp
    }

    /**
     * @param
     * @return
     * @Desc 秒杀详情信息接口
     * @Author feizi
     * @Date 2016/6/20 17:20
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (null == seckillId) {
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);
        if (null == seckill) {
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * @param
     * @return
     * @Desc AJAX调用，返回json格式数据,秒杀结果
     * @Author feizi
     * @Date 2016/6/20 17:21
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    /**
     * @Desc 执行秒杀操作
     * @Author feizi
     * @Date 2016/6/20 19:24
     * @param
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone) {
        //springMVC valid
        if(null == userPhone){
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }

        SeckillResult<SeckillExecution> result;
        try {
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            //通过调用存储过程执行秒杀操作
            SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, userPhone, md5);
            result = new SeckillResult<SeckillExecution>(true, execution);
        }catch (RepeatKillException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateConstantEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (SeckillClosedException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateConstantEnum.END);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateConstantEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
        return result;
    }

    /**
     * @Desc 获取当前系统时间
     * @Author feizi
     * @Date 2016/6/20 19:27
     * @param
     * @return
     */
    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }
}
