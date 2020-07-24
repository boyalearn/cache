package com.test.service;

import com.smart.cache.annotation.Cache;

import org.springframework.stereotype.Component;

@Component
public class Service {
    @Cache(interval = 5000,expired = 10*1000)
    public String getContext(String context){
        return "my context";
    }
}
