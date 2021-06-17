package com.cache.core.crontab;

import com.cache.core.Callback;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CallbackInfo<K, V> implements Callback<K, V>, Delayed {

    private long time;

    private Object object;

    private Method method;

    private Object[] args;

    private K key;

    private long callTime;

    private long updateTime;

    private long intervalTime;

    private long expireTime;


    @Override
    public V call(K key) {
        try {
            return (V) method.invoke(object, args);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        CallbackInfo item = (CallbackInfo) o;
        long diff = this.time - item.time;
        if (diff <= 0) {
            return -1;
        }
        return 1;
    }

}
