package com.test.jackson;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyDeserialize extends JsonDeserializer<LinkedHashMap> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public LinkedHashMap deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String json = jsonParser.getCurrentLocation().getSourceRef().toString();
        LinkedHashMap<List<String>,Object> result=new LinkedHashMap<>();
        Map map = mapper.readValue(json, Map.class);
        map.entrySet().forEach(entry->{
            System.out.println(map.get(((Map.Entry)entry).getKey()));
            map.get(((Map.Entry)entry).getKey());
        });
        return result;
    }
}
