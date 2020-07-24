package com.smart.cache;

import com.smart.cache.annotation.Cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMetaspaceBeanPostProcessor implements BeanPostProcessor {

    private static final Map<Method, Object> METHOD_CACHE = new ConcurrentHashMap<Method, Object>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        Arrays.stream(methods).forEach(method -> {
            Cache cache = method.getAnnotation(Cache.class);
            if (null != cache) {
                METHOD_CACHE.put(method,bean);
            }
        });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }
}
