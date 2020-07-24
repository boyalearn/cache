package com.test;

import com.test.config.Config;
import com.test.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CacheTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheTest.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Service service = context.getBean(Service.class);
        LOGGER.info("start");
        for (; ; ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.getContext("");
        }
    }
}
