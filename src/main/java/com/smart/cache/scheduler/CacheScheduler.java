package com.smart.cache.scheduler;

import com.smart.cache.CacheManager;
import com.smart.cache.data.CacheData;
import com.smart.cache.invoker.Invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);

    private final static MethodInvokerQueue QUEUE = new MethodInvokerQueue();

    private CacheManager cacheManager;

    private Invoker invoker;

    public CacheScheduler(CacheManager cacheManager, Invoker invoker) {
        this.cacheManager = cacheManager;
        this.invoker = invoker;
    }

    public static void execute(CacheData data) {
        long time = System.currentTimeMillis() + data.getIntervalTime();
        CacheUpdateTask task = new CacheUpdateTask(time, data);
        QUEUE.offer(task);
    }

    public void start() throws Throwable {
        do {
            CacheUpdateTask task = (CacheUpdateTask) QUEUE.take();
            CacheData cacheData = task.getCacheData();

            //判定过期的数据就不在更新了
            if (System.currentTimeMillis() - cacheData.getCallTime() > cacheData.getExpiredTime()) {
                LOGGER.error("method {} cache data expired", cacheData.getMethod());
                cacheManager.removeCacheData(cacheData.getMethod(), cacheData.getArgs());
                continue;
            }

            LOGGER.debug("update data");
            Object value = invoker.invoker(cacheData.getProcessorPoint(), cacheData.getArgs());
            cacheData.setValue(value);
            cacheData.setUpdateTime(System.currentTimeMillis());
            task.setTime(System.currentTimeMillis() + task.getCacheData().getIntervalTime());

            QUEUE.offer(task);
        } while (true);
    }
}
