package com.cache.core.cache;

import com.cache.core.Cache;
import com.cache.core.Executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EhCache<K, V> implements Cache<K, V>, Executor {

    Map data = new ConcurrentHashMap();

    @Override
    public void put(K key, V value) {
        data.put(key, value);
    }

    @Override
    public V get(K key) {
        return (V) data.get(key);
    }
}
