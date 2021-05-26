package com.cache.core.crontab;


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
        return 0L;
    }

    public Method getMethod() {
        return callMethod.getMethod();
    }

    public Long getInterval() {
        return 0L;
    }
}
