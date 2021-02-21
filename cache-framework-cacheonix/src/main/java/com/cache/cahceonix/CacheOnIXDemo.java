package com.cache.cahceonix;

import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;

public class CacheOnIXDemo {

    public static void main(String[] args) {
        Cacheonix cacheonix = Cacheonix.getInstance();

        Cache<String, String> cache = cacheonix.getCache("my.cache");
        String replacedValue = cache.put("my.key", "my.value");
        String cachedValue = cache.get("my.key");

        String removedValue = cache.remove("my.key");

        System.out.println(removedValue+cachedValue+removedValue);
    }
}
