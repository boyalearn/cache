package com.cache.core.crontab;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CacheUpdate implements Delayed {

    private long time;

    private CallInfo callInfo;

    public CacheUpdate(long time, CallInfo callInfo) {
        this.time = time;
        this.callInfo = callInfo;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        CacheUpdate item = (CacheUpdate) o;
        long diff = this.time - item.time;
        if (diff <= 0) {
            return -1;
        }
        return 1;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public CallInfo getCallInfo() {
        return callInfo;
    }
}
