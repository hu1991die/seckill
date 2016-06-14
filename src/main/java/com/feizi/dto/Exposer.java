/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.dto;

import java.io.Serializable;

/**
 * @Desc 暴露秒杀地址URL DTO数据传输层）
 * @Author feizi
 * @Date 2016/6/8 16:04
 * @Package_name com.feizi.dto
 */
public class Exposer implements Serializable{

    private static final long serialVersionUID = 2611369851821266375L;

    //是否开启
    private boolean exposed;

    //一种加密措施（主要用于验证用户的秒杀URL是否是已经被篡改之后的URL）
    private String md5;

    //秒杀id
    private long seckillId;

    //系统当前时间(单位：毫秒)
    private long nowTime;

    //秒杀开始时间
    private long startTime;

    //秒杀结束时间
    private long endTime;

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long nowTime, long startTime, long endTime) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.nowTime = nowTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
