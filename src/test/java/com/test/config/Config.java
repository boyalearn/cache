package com.test.config;

import com.smart.cache.config.EnableSmartCache;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSmartCache
@ComponentScan(value = "com.test")
public class Config {

}
