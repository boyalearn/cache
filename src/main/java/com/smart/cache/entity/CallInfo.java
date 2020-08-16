package com.smart.cache.entity;

import com.smart.cache.annotation.Cache;

import java.lang.reflect.Method;

public class CallInfo {

    private Object[] args;

    private CallMethod callMethod;

    private volatile Long callTime;

    private volatile Long updateTime;

    public Object[] getArgs() {
        return args;
    }


    public CallMethod getCallMethod() {
        return callMethod;
    }


    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }


    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public CallInfo(Object[] args, CallMethod callMethod) {
        this.args = args;
        this.callMethod = callMethod;
    }

    public Long getExpireTime() {
        return ((Cache) callMethod.getAnnotation()).expired();
    }

    public Method getMethod() {
        return callMethod.getMethod();
    }

    public Long getInterval() {
        return ((Cache) callMethod.getAnnotation()).interval();
    }
}
