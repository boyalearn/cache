package com.smart.cache.aspect;

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

    private static Map<Method, CallMethod> methodMap = new ConcurrentHashMap<Method, CallMethod>();

    @Pointcut("@annotation(com.smart.cache.annotation.Cache)")
    public void cachePointCut() {
    }

    public CacheMethodAspect(Invoker invoker) {
        this.invoker = invoker;
    }

    @Around(value = "cachePointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();
        CallMethod callMethod = methodMap.get(method);
        if (null != callMethod) {
            return invoker.invoker(callMethod, args);
        }

        Object proxy = pjp.getThis();
        Object target = pjp.getTarget();
        Cache cache = method.getAnnotation(Cache.class);
        callMethod = new CallMethod(pjp, method, target, proxy, cache.interval(), cache.expired());
        methodMap.put(method, callMethod);

        return invoker.invoker(callMethod, args);
    }
}
