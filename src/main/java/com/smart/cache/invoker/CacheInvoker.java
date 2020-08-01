package com.smart.cache.invoker;

import com.smart.cache.CacheManager;
import com.smart.cache.entity.CallInfo;
import com.smart.cache.entity.CallMethod;
import com.smart.cache.entity.InvokerCacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheInvoker implements Invoker {

    private CacheManager cacheManager;

    private Invoker invoker;

    public CacheInvoker(CacheManager cacheManager, Invoker invoker) {
        this.cacheManager = cacheManager;
        this.invoker = invoker;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheInvoker.class);

    private static final Map<InvokerCacheKey, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();

    private static final Map<InvokerCacheKey, CallInfo> callMap = new ConcurrentHashMap<InvokerCacheKey, CallInfo>();

    private Lock getLock(InvokerCacheKey key) {
        ReentrantLock lock = LOCK_MAP.get(key);
        if (null == lock) {
            synchronized (this) {
                lock = LOCK_MAP.get(key);
                if (null == lock) {
                    lock = new ReentrantLock();
                    LOCK_MAP.put(key, lock);
                }
            }
        }
        return lock;
    }

    @Override
    public Object invoker(CallMethod callMethod, Object[] args) throws Throwable {
        InvokerCacheKey cacheKey = new InvokerCacheKey(args, callMethod.getMethod());
        CallInfo callInfo = callMap.get(cacheKey);
        if (null == callInfo) {
            Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
            lock.lock();
            try {
                callInfo = callMap.get(new InvokerCacheKey(args, callMethod.getMethod()));
                if (null == callInfo) {
                    callInfo = new CallInfo(args, callMethod);
                    callInfo.setCallTime(System.currentTimeMillis());
                    callInfo.setUpdateTime(System.currentTimeMillis());
                    cacheManager.getCacheScheduler().execute(callInfo);
                    callMap.put(cacheKey, callInfo);
                }
            } finally {
                lock.unlock();
            }
        }

        if (System.currentTimeMillis() - callInfo.getCallTime() > callInfo.getExpireTime()) {
            LOGGER.error("method {} cache data expired", callInfo.getMethod());
            return callDirect(callInfo, cacheKey, callMethod, args);

        }
        return call(callInfo, cacheKey, callMethod, args);


    }

    private Object callDirect(CallInfo callInfo, InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        callInfo.setCallTime(System.currentTimeMillis());
        Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
        lock.lock();
        try {
            Object data = this.invoker.invoker(callMethod, args);
            cacheManager.getCache().put(cacheKey.toString(), data);
            return data;
        } finally {
            lock.unlock();
        }
    }

    private Object call(CallInfo callInfo, InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        callInfo.setCallTime(System.currentTimeMillis());
        Object data = cacheManager.getCache().get(new InvokerCacheKey(args, callMethod.getMethod()).toString(), callMethod.getMethod().getReturnType());
        if (null == data) {
            Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
            lock.lock();

            try {
                data = cacheManager.getCache().get(cacheKey.toString(),callMethod.getMethod().getReturnType());
                if (null == data) {
                    data = this.invoker.invoker(callMethod, args);
                    cacheManager.getCache().put(cacheKey.toString(), data);
                    return data;
                }
                LOGGER.debug("cache hit, cache data is {}", data.toString());
                return data;
            } finally {
                lock.unlock();
            }
        }
        LOGGER.debug("cache hit, cache data is {}", data.toString());
        return data;
    }
}
