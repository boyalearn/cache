package com.cache.core;

public interface CallbackCache<K, V> extends Cache<K,V>{
    V get(K key, Callback call);
}
