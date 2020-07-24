package com.smart.cache.invoker;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface Invoker {
    Object invoker(ProceedingJoinPoint pjp) throws Throwable;

    Object invoker(Method method, Object bean, Object[] args) throws Throwable;
}
