package com.cache.core;

/**
 * 缓存管理器
 */
public interface CacheManager {

    Cache getCache(String name);
}
