package com.cache.core.crontab;

import cache.CacheManager;
import cache.entity.CallInfo;
import cache.entity.InvokerCacheKey;
import cache.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);

    private final static MethodInvokerQueue QUEUE = new MethodInvokerQueue();

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(3);

    private CacheManager cacheManager;

    private Invoker invoker;

    public CacheScheduler(CacheManager cacheManager, Invoker invoker) {
        this.cacheManager = cacheManager;
        this.invoker = invoker;
    }

    public void execute(CallInfo callInfo) {
        long time = System.currentTimeMillis() + callInfo.getInterval();
        CacheUpdate task = new CacheUpdate(time, callInfo);
        QUEUE.offer(task);
    }

    public void start() throws Throwable {
        THREAD_POOL.execute(new UpdateTask(invoker));
    }

    public static void execute(Runnable runnable) {
        THREAD_POOL.execute(runnable);
    }

    public class UpdateTask implements Runnable {
        private Invoker invoker;

        public UpdateTask(Invoker invoker) {
            this.invoker = invoker;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    CacheUpdate update = (CacheUpdate) QUEUE.take();
                    CallInfo callInfo = update.getCallInfo();
                    InvokerCacheKey cacheKey = new InvokerCacheKey(callInfo.getArgs(), callInfo.getMethod());
                    //判定过期的数据就不在更新了
                    if (System.currentTimeMillis() - callInfo.getCallTime() > callInfo.getExpireTime()) {
                        LOGGER.error("method {} cache data expired", callInfo.getMethod());
                        cacheManager.getCache().remove(cacheKey.toString());
                        continue;
                    }

                    Object data = invoker.invoker(callInfo.getCallMethod(), callInfo.getArgs(), null);
                    cacheManager.getCache().put(cacheKey.toString(), data);
                    update.setTime(System.currentTimeMillis() + callInfo.getInterval());
                    callInfo.setUpdateTime(System.currentTimeMillis());
                    LOGGER.debug("interval call method {}", callInfo.getMethod());
                    QUEUE.offer(update);
                }
            } catch (Throwable e) {
                LOGGER.error(" call method exception:", e.getMessage());
            }

        }
    }
}
