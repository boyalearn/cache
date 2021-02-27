package cache.invoker;

import cache.CacheManager;
import cache.annotation.Cache;
import cache.entity.CallInfo;
import cache.entity.CallMethod;
import cache.entity.InvokerCacheKey;
import com.cache.core.crontab.CacheScheduler;
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
    public Object invoker(CallMethod callMethod, Object[] args, Object annotation) throws Throwable {
        InvokerCacheKey cacheKey = new InvokerCacheKey(args, callMethod.getMethod());
        if (annotation instanceof Cache) {
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
        } else {
            return callAsync(cacheKey, callMethod, args);
        }


    }

    private Object callDirect(CallInfo callInfo, InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        callInfo.setCallTime(System.currentTimeMillis());
        Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
        lock.lock();
        try {
            Object data = this.invoker.invoker(callMethod, args, null);
            cacheManager.getCache().put(cacheKey.toString(), data);
            return data;
        } finally {
            lock.unlock();
        }
    }

    private Object call(CallInfo callInfo, InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        callInfo.setCallTime(System.currentTimeMillis());
        return cacheLogic(cacheKey, callMethod, args);
    }

    private Object callAsync(InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        CacheScheduler.execute(()-> {
            try {
                doAsync(cacheKey, callMethod, args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return cacheLogic(cacheKey, callMethod, args);
    }

    private Object cacheLogic(InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        Object data = cacheManager.getCache().get(new InvokerCacheKey(args, callMethod.getMethod()).toString(), callMethod.getMethod().getReturnType());
        if (null == data) {
            Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
            lock.lock();
            try {
                data = cacheManager.getCache().get(cacheKey.toString(), callMethod.getMethod().getReturnType());
                if (null == data) {
                    data = this.invoker.invoker(callMethod, args, null);
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

    public void doAsync(InvokerCacheKey cacheKey, CallMethod callMethod, Object[] args) throws Throwable {
        Lock lock = getLock(new InvokerCacheKey(args, callMethod.getMethod()));
        boolean isLock = lock.tryLock();
        if (isLock) {
            try {
                Object data = this.invoker.invoker(callMethod, args, null);
                cacheManager.getCache().put(cacheKey.toString(), data);
                LOGGER.debug("cache hit, cache data is {}", data.toString());
            } finally {
                lock.unlock();
            }
        }
    }
}
