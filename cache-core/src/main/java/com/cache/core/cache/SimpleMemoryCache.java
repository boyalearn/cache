package com.cache.core.cache;

import com.cache.core.Cache;

import java.util.HashMap;
import java.util.Map;

public class SimpleMemoryCache<K, V> implements Cache<K, V> {

    private Map<K, V> cache = new HashMap<K, V>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

}
