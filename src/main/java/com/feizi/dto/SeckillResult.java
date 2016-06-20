/*
* Copyright (c) 2014 Javaranger.com. All Rights Reserved.
*/
package com.feizi.dto;

/**
 * @Desc 秒杀结果，封装JSON结果,所有的ajax请求返回类型都是json
 * @Author feizi
 * @Date 2016/6/20 17:24
 * @Package_name com.feizi.dto
 */
public class SeckillResult<T> {
    private boolean success;

    private T data;

    private String error;

    public SeckillResult(T data) {
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
