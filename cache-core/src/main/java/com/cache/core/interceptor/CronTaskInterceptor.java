package com.cache.core.interceptor;

import com.cache.core.Cache;
import com.cache.core.Callback;
import com.cache.core.Interceptor;
import com.cache.core.InvokeChain;
import com.cache.core.crontab.CronTaskContext;

import java.lang.reflect.Method;

public class CronTaskInterceptor implements Interceptor {

    private CronTaskContext cronTaskContext = new CronTaskContext();

    @Override
    public Object intercept(Cache cache, Method method, Object[] args, InvokeChain chain) {
        Object value = chain.invoke(cache, method, args);
        if (args.length != 2 || !(args[1] instanceof Callback)) {
            return value;
        }
        refresh(cache, args);
        return value;
    }

    public void refresh(Cache cache, Object[] args) {
        cache.put(args[0], ((Callback) args[1]).call(args[0]));
    }
}
