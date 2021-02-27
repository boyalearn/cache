package com.cache;

import com.cache.core.Cache;
import com.cache.core.Callback;
import com.cache.core.CallbackCache;
import com.cache.core.build.CacheBuilder;
import com.cache.core.cache.EhCache;
import com.cache.core.cache.SimpleCallBackCache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.ExpiryPolicy;
import org.junit.Before;
import org.junit.Test;

public class CallBackTest {
    private CallbackCache cache;

    @Before
    public void before(){
        CacheConfiguration<String, String> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES).offheap(500, MemoryUnit.MB)
                                .disk(1000, MemoryUnit.MB, false)
                ).withExpiry(ExpiryPolicy.NO_EXPIRY).build();

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("cache.cache")).withCache("ehCache", cacheConfiguration)
                .build(true);
        Cache innerCache = new EhCache(cacheManager.getCache("ehCache", String.class, String.class));


        cache= (CallbackCache)new CacheBuilder.Builder().cache(new SimpleCallBackCache<String,String>(innerCache)).build();
    }

    @Test
    public void testCase_00000001(){
        String value=(String)((CallbackCache)cache).get("1", new Callback<String,String>() {
            @Override
            public String call(String key) {
                return key+"1";
            }
        });
        System.out.println(value);
    }
}
