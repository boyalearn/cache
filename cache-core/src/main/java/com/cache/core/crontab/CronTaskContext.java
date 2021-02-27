package com.cache.core.crontab;

import com.cache.core.Callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CronTaskContext {
    private static final Map<String, Callback> taskContext = new ConcurrentHashMap<>();
}
