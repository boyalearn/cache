package com.test.service;

import com.smart.cache.annotation.Cache;

import org.springframework.stereotype.Component;

@Component
public class Service {

    private int i=0;

    @Cache(interval = 5000,expired = 9*1000)
    public String getContext(String context){
        return "my context"+ ++i;
    }
}
