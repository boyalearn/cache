package com.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCacheDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(100)
                .expireAfterAccess(5000, TimeUnit.MILLISECONDS)
                .build();

        cache.put("2","3");

        Object o = cache.get("2", () -> createExpensiveGraph("2"));

        System.out.println(o);

        Thread.sleep(10*1000);

        o = cache.get("2", () -> createExpensiveGraph("2"));

        System.out.println(o);

    }

    public static String createExpensiveGraph(String key) {
        System.out.println("load into cache!");
        return "hello " + key + "!";
    }
}
