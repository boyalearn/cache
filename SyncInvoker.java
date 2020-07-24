package com.smart.cache.invoker;

import com.smart.cache.CacheManager;
import com.smart.cache.CacheMethodInfo;
import com.smart.cache.annotation.Cache;
import com.smart.cache.data.CacheData;
import com.smart.cache.scheduler.CacheScheduler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncInvoker implements Invoker {

    private final static Logger LOGGER = LoggerFactory.getLogger(SyncInvoker.class);

    private CacheManager cacheManager;

    public SyncInvoker(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private static final Map<Method, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();

    @Override
    public Object invoker(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        Object[] args = pjp.getArgs();
        CacheData cacheData = cacheManager.getCacheData(method, args);
        //双检锁的实现逻辑
        if (null == cacheData) {
            getLock(getMethod(pjp)).lock();
            try {
                cacheData = cacheManager.getCacheData(method, args);
                if (null == cacheData) {
                    return doProcess(pjp, args);
                } else {
                    return doCacheData(cacheData, pjp, args);
                }
            } finally {
                getLock(getMethod(pjp)).unlock();
            }
        } else {
            return doCacheData(cacheData, pjp, args);
        }
    }

    @Override
    public Object invoker(Method method, Object bean, Object[] args) throws Throwable {
        Lock lock = getLock(method);
        lock.lock();

        try {
            return method.invoke(bean, args);
        } finally {
            lock.unlock();
        }
    }

    private Lock getLock(Method method) {
        ReentrantLock lock = LOCK_MAP.get(method);
        if (null == lock) {
            synchronized (this) {
                lock = LOCK_MAP.get(method);
                if (null == lock) {
                    lock = new ReentrantLock();
                    LOCK_MAP.put(method, lock);
                }
            }
        }
        return lock;
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }

    public static String getMethodName(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod().toString();
    }

    private Object doCacheData(CacheData cacheData, ProceedingJoinPoint pjp, Object[] args) throws Throwable {
        if (cacheValid(cacheData)) {
            LOGGER.debug("cache hit. cache data is {}", cacheData.getValue());
            return cacheData.getValue();
        } else {
            return doProcess(pjp, args);
        }
    }

    private Object doProcess(ProceedingJoinPoint pjp, Object[] args) throws Throwable {
        Object result = pjp.proceed(pjp.getArgs());
        LOGGER.debug("cache hit failed. cache data is {}", result);

        CacheMethodInfo methodInfo = new CacheMethodInfo();
        Object bean = pjp.getThis();
        Method method = getMethod(pjp);
        Cache cacheAnn = method.getAnnotation(Cache.class);
        methodInfo.setExpiredTime(cacheAnn.expired());
        methodInfo.setIntervalTime(cacheAnn.interval());
        methodInfo.setMethod(method);
        methodInfo.setBean(bean);

        CacheData cacheData = new CacheData(args, System.currentTimeMillis(), System.currentTimeMillis(),
            result, methodInfo);
        cacheManager.addCacheData(method, args, cacheData);

        CacheScheduler.execute(cacheData);
        return result;
    }

    private boolean cacheValid(CacheData cacheData) {
        if (System.currentTimeMillis() - cacheData.getCallTime() - cacheData.getExpiredTime() > 0) {
            LOGGER.debug("cache data:{} is expired.", cacheData.getValue());
            return false;
        } else {
            return true;
        }
    }
}
