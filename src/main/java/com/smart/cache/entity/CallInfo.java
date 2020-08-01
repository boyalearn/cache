package com.smart.cache.entity;

import java.lang.reflect.Method;

public class CallInfo {

    private Object[] args;

    private CallMethod callMethod;

    private volatile Long callTime;

    private volatile Long updateTime;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public CallMethod getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(CallMethod callMethod) {
        this.callMethod = callMethod;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public CallInfo(Object[] args, CallMethod callMethod) {
        this.args = args;
        this.callMethod = callMethod;
    }

    public Long getExpireTime() {
        return callMethod.getExpire();
    }

    public Method getMethod() {
        return callMethod.getMethod();
    }

    public Long getInterval(){
        return callMethod.getInterval();
    }
}
