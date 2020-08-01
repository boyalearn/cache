package com.smart.cache.data;

public interface Cache {

    void put(String key, Object value);

    Object get(String key);

    Object remove(String key);
}
