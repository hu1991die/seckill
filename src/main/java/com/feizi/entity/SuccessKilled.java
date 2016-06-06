/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 成功秒杀明细表实体类
 * @Author feizi
 * @Date 2016/6/4 19:16
 * @Package_name com.feizi.entity
 */
public class SuccessKilled implements Serializable {

    private static final long serialVersionUID = -3010221086744444867L;

    //秒杀商品id
    private long seckillId;

    //用户手机号
    private long user_phone;

    //状态标识（-1:无效，0:成功,1:已付款）
    private short state;

    //创建时间
    private Date createTime;

    //变通
    //多对一
    private Seckill seckill;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(long user_phone) {
        this.user_phone = user_phone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", user_phone=" + user_phone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
