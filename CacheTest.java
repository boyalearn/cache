package com.test;

import com.test.config.Config;
import com.test.service.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheTest {

    private final static Logger LOGGER=LoggerFactory.getLogger(CacheTest.class);
    private final static ThreadFactory namedThreadFactory = new CustomizableThreadFactory("cache-update");

    private final static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 10,
        TimeUnit.MINUTES, new LinkedBlockingQueue<>(), namedThreadFactory);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Service service = context.getBean(Service.class);
        LOGGER.info("start");
        for (;;) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.getContext("");
        }
    }
}
