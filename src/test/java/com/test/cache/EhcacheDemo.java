package com.test.cache;

import org.ehcache.Cache;

public class EhcacheDemo {
    public static void main(String[] args) {
        Cache<String, String> cache = createPersistentCache();
        for (int i = 0; i < 1000; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.println(cache.get("key3"));
    }

    public static Cache<String, String> createPersistentCache() {
        return null;
    }
}
