package com.cache.core;

@FunctionalInterface
public interface Callback<K, V> {

    V call(K key);
}
