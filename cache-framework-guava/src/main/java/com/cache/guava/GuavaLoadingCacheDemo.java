package com.cache.guava;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaLoadingCacheDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        LoadingCache<String,String> cacheBuilder= CacheBuilder
                .newBuilder().maximumSize(4).expireAfterWrite(2, TimeUnit.SECONDS)
                .removalListener(new RemovalListener(){

                    @Override
                    public void onRemoval(RemovalNotification removalNotification) {
                        System.out.println("this key is "+removalNotification.getKey()+" removed");
                    }
                })
                .build(new CacheLoader<String, String>(){
                    @Override
                    public String load(String key) throws Exception {
                        return createExpensiveGraph(key);
                    }

                });

        cacheBuilder.put("1","23");

        System.out.println(cacheBuilder.get("1"));
        System.out.println(cacheBuilder.get("2"));
        System.out.println(cacheBuilder.get("3"));
        System.out.println(cacheBuilder.get("2"));
        Thread.sleep(4*1000);
        System.out.println(cacheBuilder.get("2"));
        System.out.println(cacheBuilder.get("1"));
        Thread.sleep(2*1000);
    }

    public static String createExpensiveGraph(String key) {
        System.out.println("load into cache!");
        return "hello "+key+"!";
    }
}
