package com.cache.core;

import java.lang.reflect.Method;

public interface InvokeChain{
    Object invoke(Object obj, Method method, Object[] args);
    void reuse();
}
