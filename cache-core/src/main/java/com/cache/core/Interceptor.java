package com.cache.core;

import java.lang.reflect.Method;

public interface Interceptor {
    Object intercept(Object cache, Method method, Object[] args, InvokeChain chain);
}
