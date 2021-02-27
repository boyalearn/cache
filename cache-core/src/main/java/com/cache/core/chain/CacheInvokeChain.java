package com.cache.core.chain;

import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;

import java.lang.reflect.Method;
import java.util.List;

public class CacheInvokeChain implements InvokeChain {

    private static final ThreadLocal<Integer> indexCount = new ThreadLocal<>();

    private int n = 0;

    private Interceptor[] interceptors;

    public CacheInvokeChain(List<Interceptor> interceptors) {
        this.interceptors = interceptors.toArray(new Interceptor[]{});
        indexCount.set(0);
        n = interceptors.size();
    }


    @Override
    public Object invoke(Object obj, Method method, Object[] args) {
        return internalDoInvoke(obj, method, args);
    }

    private Object internalDoInvoke(Object obj, Method method, Object[] args) {
        Interceptor interceptor = getInterceptor();
        if (null == interceptor) {
            try {
                return method.invoke(obj, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return interceptor.intercept(obj, method, args, this);
    }

    private Interceptor getInterceptor() {
        if (interceptors == null || interceptors.length == 0) {
            return null;
        }
        int pos = indexCount.get();
        if (pos < n) {
            indexCount.set(pos + 1);
            return interceptors[pos];
        }
        return null;
    }

    @Override
    public void reuse() {
        indexCount.set(0);
    }

}
