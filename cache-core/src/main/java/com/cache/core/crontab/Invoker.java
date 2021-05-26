package com.cache.core.crontab;


public interface Invoker {
    Object invoker(CallMethod callMethod, Object[] args, Object annotation) throws Throwable;
}
