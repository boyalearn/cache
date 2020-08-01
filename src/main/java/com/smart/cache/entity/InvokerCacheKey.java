package com.smart.cache.entity;

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
        StringBuffer sb = new StringBuffer("InvokerCacheKey{args={");
        for (Object arg : args) {
            sb.append(arg.toString() + ",");
        }
        sb.replace(sb.length() - 1, sb.length(), "}");
        sb.append(",method=" + method.toString() + "}");
        return sb.toString();
    }
}
