package com.test.service;

import com.smart.cache.annotation.AsyncCache;
import com.smart.cache.annotation.Cache;

import org.springframework.stereotype.Component;

@Component
public class Service {

    private int i = 0;

    //@Cache(interval = 5000, expired = 9 * 1000)
    @AsyncCache
    public Object getContext()  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "dfdsfd";


    }
}
