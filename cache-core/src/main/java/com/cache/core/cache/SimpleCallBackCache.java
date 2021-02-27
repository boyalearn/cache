package com.cache.core.cache;

import com.cache.core.Cache;
import com.cache.core.Callback;
import com.cache.core.CallbackCache;

public class SimpleCallBackCache<K, V> implements CallbackCache<K, V> {

    private Cache<K, V> cache;

    public SimpleCallBackCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public V get(K key, Callback call) {
        V value = cache.get(key);
        if (value == null) {
            value = (V) call.call(key);
            cache.put(key, value);
        }
        return value;
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
