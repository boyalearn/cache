package cache;

import com.smart.cache.data.Cache;
import com.smart.cache.scheduler.CacheScheduler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class CacheManager implements ApplicationListener<ContextRefreshedEvent> {

    private CacheScheduler cacheScheduler;

    private Cache cache;

    public CacheManager(Cache cache) {
        this.cache = cache;
    }

    public void setCacheScheduler(CacheScheduler cacheScheduler) {
        this.cacheScheduler = cacheScheduler;
    }

    public Cache getCache() {
        return cache;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(() -> {
            try {
                cacheScheduler.start();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }).start();
    }

    public CacheScheduler getCacheScheduler() {
        return cacheScheduler;
    }
}
