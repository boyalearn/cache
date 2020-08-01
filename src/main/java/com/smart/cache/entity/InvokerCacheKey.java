package com.smart.cache.entity;

import com.smart.cache.serialize.SerializeUtil;

import java.lang.reflect.Method;

public class InvokerCacheKey {
    private Object[] args;

    private Method method;

    public InvokerCacheKey(Object[] args, Method method) {
        this.args = args;
        this.method = method;
    }

    @Override
    public int hashCode() {
        int hashCode = 007;
        for (Object arg : args) {
            hashCode = hashCode * 31 + arg.hashCode();
        }
        hashCode = hashCode * 31 + method.toString().hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return toString().equals(obj.toString());

    }

    @Override
    public String toString() {
        return SerializeUtil.encode(this.args) + SerializeUtil.encode(method.toString());
    }
}
