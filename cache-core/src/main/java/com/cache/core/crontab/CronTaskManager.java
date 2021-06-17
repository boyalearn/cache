package com.cache.core.crontab;

import com.cache.core.Cache;
import lombok.Getter;

import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class CronTaskManager {
    private MethodInvokerQueue queue = new MethodInvokerQueue();

    private CopyOnWriteArraySet<CallbackInfo> schedulerSet = new CopyOnWriteArraySet<>();

    private Cache cache;


    public CronTaskManager(Cache cache) {
        new CacheScheduler(this, cache).start();
        this.cache = cache;
    }

    public boolean addCallbackInfo(CallbackInfo callbackInfo) {
        if (schedulerSet.contains(callbackInfo)) {
            return false;
        }
        schedulerSet.add(callbackInfo);
        queue.offer(callbackInfo);
        return true;
    }

}
