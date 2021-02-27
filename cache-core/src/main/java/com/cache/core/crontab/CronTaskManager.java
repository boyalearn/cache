package com.cache.core.crontab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CronTaskManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);

    private final static MethodInvokerQueue QUEUE = new MethodInvokerQueue();

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(3);
}
