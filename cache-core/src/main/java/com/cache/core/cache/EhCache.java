package com.cache.core.cache;

import com.cache.core.Cache;
import com.cache.core.Executor;

public class EhCache<K, V> implements Cache<K, V>, Executor {


    private org.ehcache.Cache<K, V> cache;

    public EhCache(org.ehcache.Cache<K, V> cache) {
        if (null == cache) {
            throw new RuntimeException("cache is null");
        }
        this.cache = cache;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }
}
