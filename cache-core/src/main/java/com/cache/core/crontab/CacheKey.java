package com.cache.core.crontab;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class CacheKey {

    private Method method;

    private Object[] args;


}
