package com.smart.cache.data;

import com.smart.cache.serialize.SerializeUtil;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.ExpiryPolicy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class Ehcache implements Cache, DisposableBean {

    @Value("${cache.smart.size:2000}")
    private Long size;

    @Value("${cache.smart.diskSize:500}")
    private Long diskSize;

    @Value("${cache.smart.offHeapSize:100}")
    private Long offHeapSize;

    @Value("${cache.smart.size:D:\\ehcacheData}")
    private String filePath;

    private org.ehcache.Cache<String, String> cache;

    private CacheManager cacheManager;

    public Ehcache() {
    }

    @PostConstruct
    public void init() {
        CacheConfiguration<String, String> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder
                        .newResourcePoolsBuilder().heap(size, EntryUnit.ENTRIES).offheap(offHeapSize, MemoryUnit.MB)
                        .disk(diskSize, MemoryUnit.MB, false)
                ).withExpiry(ExpiryPolicy.NO_EXPIRY).build();

        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(filePath)).withCache("ehCache", cacheConfiguration)
                .build(true);

        this.cache = this.cacheManager.getCache("ehCache", String.class, String.class);
    }

    @Override
    public void put(String key, Object value) {
        this.cache.put(key, SerializeUtil.encode(value));
    }

    @Override
    public Object get(String key, Class<?> clazz) {
        return SerializeUtil.decode(cache.get(key), clazz);
    }

    @Override
    public Object remove(String key) {
        Object value = cache.get(key);
        cache.remove(key);
        return value;
    }

    @Override
    public void destroy() throws Exception {
        cacheManager.close();
    }
}
