package com.smart.cache.config;

import com.smart.cache.CacheManager;
import com.smart.cache.aspect.CacheMethodAspect;
import com.smart.cache.invoker.Invoker;
import com.smart.cache.invoker.SyncInvoker;
import com.smart.cache.scheduler.CacheScheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartCacheConfiguration {

    @Bean
    public CacheMethodAspect cacheMethodAspect(Invoker invoker) {
        return new CacheMethodAspect(invoker);
    }

    @Bean
    public Invoker invoker(CacheManager cacheManager) {
        return new SyncInvoker(cacheManager);
    }

    @Bean
    public CacheManager cacheManager() {
        return new CacheManager();
    }

    @Bean
    public CacheScheduler cacheScheduler(CacheManager cacheManager, Invoker invoker) {
        CacheScheduler cacheScheduler = new CacheScheduler(cacheManager, invoker);
        cacheManager.setCacheScheduler(cacheScheduler);
        return cacheScheduler;
    }
}
