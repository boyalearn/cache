package com.cache.core;

public interface CallbackCache<K, V> extends Cache{
    V get(K key, Callback call);
}
