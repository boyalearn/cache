package com.smart.cache.data;

import com.smart.cache.CacheMethodInfo;

import java.lang.reflect.Method;

public class CacheData {
    private Object[] args;

    private Long updateTime;

    private Long callTime;

    private Object value;

    private CacheMethodInfo cacheMethodInfo;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public CacheMethodInfo getCacheMethodInfo() {
        return cacheMethodInfo;
    }

    public void setCacheMethodInfo(CacheMethodInfo cacheMethodInfo) {
        this.cacheMethodInfo = cacheMethodInfo;
    }

    public CacheData(Object[] args, Long updateTime, Long callTime, Object value,
        CacheMethodInfo cacheMethodInfo) {
        this.args = args;
        this.updateTime = updateTime;
        this.callTime = callTime;
        this.value = value;
        this.cacheMethodInfo = cacheMethodInfo;
    }

    public Long getExpiredTime() {
        return cacheMethodInfo.getExpiredTime();
    }

    public Long getIntervalTime() {
        return cacheMethodInfo.getIntervalTime();
    }

    public Method getMethod() {
        return cacheMethodInfo.getMethod();
    }

    public Object getBean() {
        return cacheMethodInfo.getBean();
    }
}
