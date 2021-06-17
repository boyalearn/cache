package com.cache.core.build;

import com.cache.core.Cache;
import com.cache.core.Callback;
import com.cache.core.Executor;
import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;
import com.cache.core.cache.SimpleMemoryCache;
import com.cache.core.chain.CacheInvokeChain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class CacheBuilder {


    public static class Builder {
        private Cache cache = new SimpleMemoryCache();

        private List<Interceptor> interceptors = new ArrayList<>();

        public Builder cache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder interceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public Cache build() {
            CacheInvokeChain cacheInvokeChain = new CacheInvokeChain(interceptors);
            SimpleCache simpleCache = new SimpleCache(cache, cacheInvokeChain);
            return (Cache) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Cache.class}, simpleCache);
        }
    }

    public static class SimpleCache<K, V> implements InvocationHandler, Executor<K, V> {

        private Cache<K, V> cache;

        private InvokeChain invokeChain;

        SimpleCache(Cache cache, InvokeChain invokeChain) {
            this.cache = cache;
            this.invokeChain = invokeChain;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            invokeChain.reuse();
            return invokeChain.invoke(this, method, args);
        }

        @Override
        public V get(K key, Callback<K, V> callback) {
            V v = cache.get(key);
            if (null == v) {
                return callback.call(key);
            }
            return v;
        }

        @Override
        public void put(K key, V value) {
            cache.put(key, value);
        }

        @Override
        public V get(K key) {
            return cache.get(key);
        }
    }
}
