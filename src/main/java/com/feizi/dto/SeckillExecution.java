/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.dto;

import com.feizi.constants.SeckillStateConstantEnum;
import com.feizi.entity.SuccessKilled;

/**
 * @Desc 封装秒杀执行后结果
 * @Author feizi
 * @Date 2016/6/8 16:13
 * @Package_name com.feizi.dto
 */
public class SeckillExecution {

    //秒杀id
    private long seckillId;

    //秒杀执行结果状态（0-秒杀失败，1-秒杀成功）
    private int state;

    //状态标识
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStateConstantEnum seckillStateConstantEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStateConstantEnum.getState();
        this.stateInfo = seckillStateConstantEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStateConstantEnum seckillStateConstantEnum) {
        this.seckillId = seckillId;
        this.state = seckillStateConstantEnum.getState();
        this.stateInfo = seckillStateConstantEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
