package com.test.service;

import com.smart.cache.annotation.Cache;

import org.springframework.stereotype.Component;

@Component
public class Service {
    @Cache(interval = 20000,expired = 40*1000)
    public String getContext(String context){
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "my context";
    }
}
