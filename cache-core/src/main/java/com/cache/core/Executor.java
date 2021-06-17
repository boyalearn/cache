package com.cache.core;

public interface Executor<K, V> extends Cache<K, V> {
    V get(K key, Callback<K, V> callback);
}