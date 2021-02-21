package com.cache.core;

import java.util.concurrent.ConcurrentHashMap;

public class SynchronizeCache implements Cache {


    private final ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap();


    private Cache cache;

    public SynchronizeCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return cache.get(key);
    }
}
