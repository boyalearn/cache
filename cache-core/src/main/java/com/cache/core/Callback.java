package com.cache.core;

@FunctionalInterface
public interface Callback<V> {

    V call();
}
