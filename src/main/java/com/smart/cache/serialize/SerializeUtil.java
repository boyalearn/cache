package com.smart.cache.serialize;

import com.google.gson.Gson;

public final class SerializeUtil {

    public static final String encode(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static final Object decode(String context, Class<?> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(context, clazz);
    }
}
