package com.cache;

import com.cache.core.Cache;
import com.cache.core.Interceptor;
import com.cache.core.build.CacheBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    Cache cache;

    @Before
    public void before() {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new CacheInterceptor());
        interceptors.add(new CachePreInterceptor());
        cache = new CacheBuilder.Builder().interceptors(interceptors).build();
        cache.put("1", "2");
    }


    @Test
    public void testCase_00000001() {
        System.out.println(cache.get("1"));
        System.out.println("2".equals(cache.get(1)));
    }


}
