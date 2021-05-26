package com.cache.core.crontab;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class CallMethod {

    private Method method;


    private Object annotation;

    private ProceedingJoinPoint processor;

    public Method getMethod() {
        return method;
    }


    public CallMethod(ProceedingJoinPoint processor, Method method, Object annotation) {
        this.processor = processor;
        this.method = method;
        this.annotation = annotation;
    }

    public Object invoker(Object[] args) throws Throwable {
        return this.processor.proceed(args);
    }

    public Object getAnnotation() {
        return annotation;
    }

}
