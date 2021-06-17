package com.cache.core.crontab;

import lombok.Getter;

import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class CronTaskManager {
    private MethodInvokerQueue queue = new MethodInvokerQueue();

    private CopyOnWriteArraySet<CallbackInfo> schedulerSet = new CopyOnWriteArraySet<>();


    public CronTaskManager() {
        new CacheScheduler(this).start();
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
