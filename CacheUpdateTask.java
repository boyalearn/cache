package com.smart.cache.scheduler;

import com.smart.cache.CacheMethodInfo;
import com.smart.cache.data.CacheData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CacheUpdateTask implements Delayed {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheUpdateTask.class);

    private long time;

    private CacheData cacheData;

    public CacheUpdateTask(long time, CacheData cacheData) {
        this.time = time;
        this.cacheData = cacheData;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        CacheUpdateTask item = (CacheUpdateTask) o;
        long diff = this.time - item.time;
        if (diff <= 0) {
            return -1;
        }
        return 1;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public CacheData getCacheData() {
        return cacheData;
    }
}
