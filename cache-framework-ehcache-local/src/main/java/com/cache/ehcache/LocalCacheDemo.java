package com.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;


public class LocalCacheDemo {
    public static void main(String[] args) throws Exception {
        //从配置文件创建配置对象
        Configuration xmlConf = new XmlConfiguration(LocalCacheDemo.class.getResource("/LocalEhcache.xml"));
        //创建缓存管理器
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConf);
        cacheManager.init();
        Cache<String, String> cache = cacheManager.getCache("cache", String.class, String.class);
        cache.put("1", "2");
        System.out.println(cache.get("1"));
    }
}
