package cache.data;

public interface Cache {

    void put(String key, Object value);

    Object get(String key, Class<?> clazz);

    Object remove(String key);
}
