package com.cache.core.build;

import com.cache.core.Cache;
import com.cache.core.CallbackCache;
import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;
import com.cache.core.chain.CacheInvokeChain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class CacheBuilder {


    public static class Builder {
        private Cache cache;
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
            Class<?>[] interfaces = cache.getClass().getInterfaces();
            for(Class currentInterface :interfaces){
                if(currentInterface == CallbackCache.class){
                    return (CallbackCache) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{CallbackCache.class}, simpleCache);
                }else if(currentInterface == Cache.class){
                    return (Cache) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Cache.class}, simpleCache);
                }
            }
            return null;
        }
    }

    public static class SimpleCache implements InvocationHandler {

        private Cache cache;
        private InvokeChain invokeChain;

        SimpleCache(Cache cache, InvokeChain invokeChain) {
            this.cache = cache;
            this.invokeChain = invokeChain;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            invokeChain.reuse();
            return invokeChain.invoke(cache, method, args);
        }

    }
}
