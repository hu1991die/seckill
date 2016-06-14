/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.constants;

/**
 * @Desc 使用枚举类型标示常量数据字典
 * @Author feizi
 * @Date 2016/6/8 18:11
 * @Package_name com.feizi.constants
 */
public enum SeckillStateConstantEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATE_REWRITE(-3, "数据篡改")
    ;
    private int state;

    private String stateInfo;

    SeckillStateConstantEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateConstantEnum stateOf(int index){
        for (SeckillStateConstantEnum seckillStateConstant : values()){
            if(seckillStateConstant.getState() == index){
                return seckillStateConstant;
            }
        }
        return null;
    }
}
