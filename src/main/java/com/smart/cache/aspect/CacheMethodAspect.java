package com.smart.cache.aspect;

import com.smart.cache.annotation.AsyncCache;
import com.smart.cache.annotation.Cache;
import com.smart.cache.entity.CallMethod;
import com.smart.cache.invoker.Invoker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Aspect
public class CacheMethodAspect {

    @Autowired
    private Invoker invoker;

    private static Map<String, CallMethod> methodMap = new ConcurrentHashMap<String, CallMethod>();

    @Pointcut("@annotation(com.smart.cache.annotation.Cache)")
    public void cachePointCut() {
    }

    @Pointcut("@annotation(com.smart.cache.annotation.AsyncCache)")
    public void asyncCachePointCut() {
    }

    public CacheMethodAspect(Invoker invoker) {
        this.invoker = invoker;
    }

    @Around(value = "cachePointCut()||asyncCachePointCut()")
    public Object doCacheAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();
        Cache cache = method.getAnnotation(Cache.class);
        if (null != cache) {
            return doInvoker(method, pjp, args, cache);
        }
        AsyncCache asyncCache = method.getAnnotation(AsyncCache.class);
        return doInvoker(method, pjp, args, asyncCache);
    }

    private Object doInvoker(Method method, ProceedingJoinPoint pjp, Object[] args, Object cache) throws Throwable {
        CallMethod callMethod = methodMap.get(method);
        if (null != callMethod) {
            return invoker.invoker(callMethod, args, cache);
        }
        callMethod = new CallMethod(pjp, method, cache);
        methodMap.put(method.getName() + method.getParameterTypes(), callMethod);
        return invoker.invoker(callMethod, args, cache);
    }

}
