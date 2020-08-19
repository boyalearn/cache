package com.test.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MySerializer extends JsonSerializer<LinkedHashMap> {

    @Override
    public void serialize(LinkedHashMap linkedHashMap, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        linkedHashMap.entrySet().forEach(entry->{
            try {
                jsonGenerator.writeObjectField("key",((Map.Entry)entry).getKey());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
