package com.smart.cache.aspect;

import com.smart.cache.invoker.Invoker;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class CacheMethodAspect {

    private Invoker invoker;

    @Pointcut("@annotation(com.smart.cache.annotation.Cache)")
    public void cachePointCut() {
    }

    public CacheMethodAspect(Invoker invoker) {
        this.invoker = invoker;
    }

    @Around(value = "cachePointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return invoker.invoker(pjp);
    }
}
