package com.cache;

import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;

import java.lang.reflect.Method;

public class CachePreInterceptor implements Interceptor {
    @Override
    public Object intercept(Object cache, Method method, Object[] args, InvokeChain chain) {
        System.out.println("pre方法拦截");
        return chain.invoke(cache,method,args);
    }
}
