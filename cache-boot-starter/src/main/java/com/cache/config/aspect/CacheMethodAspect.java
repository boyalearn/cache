package com.cache.config.aspect;

import com.cache.config.annotation.Cache;
import com.cache.core.crontab.CallbackInfo;
import com.cache.core.crontab.CronTaskManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

@Aspect
public class CacheMethodAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Pointcut("@annotation(com.cache.config.annotation.Cache)")
    public void cachePointCut() {
    }


    @Around(value = "cachePointCut()")
    public Object doCacheAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();
        Cache cache = method.getAnnotation(Cache.class);
        if (null != cache) {
            return doInvoker(method, pjp, args, cache);
        }
        return pjp.proceed(args);
    }

    private Object doInvoker(Method method, ProceedingJoinPoint pjp, Object[] args, Cache cache) throws Throwable {
        CronTaskManager manager = applicationContext.getBean(CronTaskManager.class);
        CallbackInfo callbackInfo=new CallbackInfo();
        Object target = pjp.getTarget();
        callbackInfo.setObject(target);
        callbackInfo.setMethod(method);
        callbackInfo.setArgs(args);
        callbackInfo.setIntervalTime(cache.intervalTime());
        callbackInfo.setExpireTime(cache.expireTime());
        manager.addCallbackInfo(callbackInfo);
        return pjp.proceed(args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
