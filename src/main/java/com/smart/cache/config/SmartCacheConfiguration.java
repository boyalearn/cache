package com.smart.cache.config;

import com.smart.cache.CacheManager;
import com.smart.cache.aspect.CacheMethodAspect;
import com.smart.cache.data.Cache;
import com.smart.cache.data.Ehcache;
import com.smart.cache.invoker.CacheInvoker;
import com.smart.cache.invoker.DirectInvoker;
import com.smart.cache.invoker.Invoker;
import com.smart.cache.scheduler.CacheScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SmartCacheConfiguration {

    @Bean
    public CacheMethodAspect cacheMethodAspect(Invoker invoker) {
        return new CacheMethodAspect(invoker);
    }

    @Bean
    public DirectInvoker directInvoker() {
        return new DirectInvoker();
    }

    @Bean
    @Primary
    public CacheInvoker cacheInvoker(CacheManager cacheManager, DirectInvoker directInvoker) {
        return new CacheInvoker(cacheManager, directInvoker);
    }

    @Bean
    @Conditional(value = OnNoCacheCondition.class)
    public Cache cache() {
        return new Ehcache();
    }

    @Bean
    public CacheManager cacheManager(Cache cache) {
        return new CacheManager(cache);
    }

    @Bean
    public CacheScheduler cacheScheduler(CacheManager cacheManager, DirectInvoker invoker) {
        CacheScheduler cacheScheduler = new CacheScheduler(cacheManager, invoker);
        cacheManager.setCacheScheduler(cacheScheduler);
        return cacheScheduler;
    }
}
