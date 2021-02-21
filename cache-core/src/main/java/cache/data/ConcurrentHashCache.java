package cache.data;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashCache implements Cache {

    private static ConcurrentHashMap cache = new ConcurrentHashMap();

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key, Class<?> clazz) {
        return cache.get(key);
    }

    @Override
    public Object remove(String key) {
        return cache.remove(key);
    }
}
