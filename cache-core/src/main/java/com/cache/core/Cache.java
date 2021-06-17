package com.cache.core;

/**
 * 缓存
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {

    void put(K key, V value);

    V get(K key);
}
