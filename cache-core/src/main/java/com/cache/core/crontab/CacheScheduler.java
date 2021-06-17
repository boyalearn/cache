package com.cache.core.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheScheduler implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);

    private static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(3);

    private CronTaskManager cronTaskManager;


    public CacheScheduler(CronTaskManager cronTaskManager) {
        this.cronTaskManager = cronTaskManager;
    }

    public void start() {
        this.THREAD_POOL.submit(this);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                CallbackInfo callbackInfo = (CallbackInfo) cronTaskManager.getQueue().take();
                //判定过期的数据就不在更新了
                if (System.currentTimeMillis() - callbackInfo.getCallTime() > callbackInfo.getExpireTime()) {
                    LOGGER.error("method {} cache data expired", callbackInfo.getMethod());
                    continue;
                }

                THREAD_POOL.submit(() -> {
                    callbackInfo.call(callbackInfo.getKey());
                    callbackInfo.setTime(System.currentTimeMillis() + callbackInfo.getIntervalTime());
                    callbackInfo.setUpdateTime(System.currentTimeMillis());
                    LOGGER.debug("interval call method {}", callbackInfo.getMethod());
                    cronTaskManager.getQueue().offer(callbackInfo);
                });
            }
        } catch (Throwable e) {
            LOGGER.error(" call method exception:", e.getMessage());
        }
    }
}
