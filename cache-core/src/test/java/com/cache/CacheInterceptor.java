package com.cache;

import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;

import java.lang.reflect.Method;


public class CacheInterceptor implements Interceptor {
    @Override
    public Object intercept(Object cache, Method method, Object[] args, InvokeChain chain) {
        System.out.println("方法拦截1");
        return chain.invoke(cache, method, args);
    }
}
