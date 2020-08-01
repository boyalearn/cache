package com.smart.cache.entity;

import org.aspectj.lang.ProceedingJoinPoint;
import java.lang.reflect.Method;

public class CallMethod {

    private Method method;

    private Object target;

    private Object proxy;

    private Long interval;

    private Long expire;

    private ProceedingJoinPoint processor;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public CallMethod(ProceedingJoinPoint processor, Method method, Object target, Object proxy, Long interval, Long expire) {
        this.processor = processor;
        this.method = method;
        this.target = target;
        this.proxy = proxy;
        this.interval = interval;
        this.expire = expire;
    }

    public Object invoker(Object[] args) throws Throwable {
        return this.processor.proceed(args);
    }
}
