package com.smart.cache;

import com.smart.cache.data.CacheData;
import com.smart.cache.scheduler.CacheScheduler;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager implements ApplicationListener<ContextRefreshedEvent> {

    private CacheScheduler cacheScheduler;

    private final static ConcurrentHashMap<String, CacheData> CACHE = new ConcurrentHashMap<String, CacheData>();

    public CacheManager(CacheScheduler cacheScheduler) {
        this.cacheScheduler = cacheScheduler;
    }

    public CacheManager() {
    }

    public void setCacheScheduler(CacheScheduler cacheScheduler) {
        this.cacheScheduler = cacheScheduler;
    }

    public ConcurrentHashMap<String, CacheData> getCache() {
        return CACHE;
    }

    private String getCacheKey(Method method, Object[] args) {
        StringBuffer sb = new StringBuffer("M_" + method.getName() + "P_");
        Arrays.stream(args).forEach(arg -> sb.append(arg.toString()));
        return sb.toString();
    }

    public CacheData getCacheData(Method method, Object[] args) {
        return CACHE.get(getCacheKey(method,args));
    }

    public void addCacheData(Method method, Object[] args, CacheData cacheData) {
        CACHE.put(getCacheKey(method, args), cacheData);
    }

    public void removeCacheData(Method method, Object[] args) {
        CACHE.remove(getCacheKey(method, args));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cacheScheduler.start();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }


}
