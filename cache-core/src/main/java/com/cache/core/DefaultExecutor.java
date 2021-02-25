package com.cache.core;

public class DefaultExecutor<K,V> implements Executor,Cache<K,V> {

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V get(K key) {
        return null;
    }
}
