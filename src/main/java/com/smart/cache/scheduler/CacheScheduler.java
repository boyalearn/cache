package com.smart.cache.scheduler;

import com.smart.cache.CacheManager;
import com.smart.cache.entity.CallInfo;
import com.smart.cache.entity.InvokerCacheKey;
import com.smart.cache.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);

    private final static MethodInvokerQueue QUEUE = new MethodInvokerQueue();

    private Long threadNum=2L;

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
        for(int i=0;i<threadNum;i++){
            new Thread(new UpdateTask(invoker), "cache-update-thread-"+i).start();
        }
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
                    Object data=invoker.invoker(callInfo.getCallMethod(), callInfo.getArgs());
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
