package com.test.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JacksonTest {
    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper mapper=new ObjectMapper();

        MapBean mapBean=new MapBean("");
        LinkedHashMap<List<String>, Object>  map=new LinkedHashMap<List<String>,Object>();
        List<String> list1=new ArrayList<>();
        list1.add("keyOne1");
        list1.add("keyTwo1");
        List<String> list2=new ArrayList<>();
        list2.add("keyOne2");
        list2.add("keyTwo2");
        map.put(list1,new Auth("one","two"));
        map.put(list2,new Auth("one","two"));

        mapBean.setMap(map);
        String s = mapper.writeValueAsString(mapBean);
        System.out.println(s);

        MapBean mapBean1 = mapper.readValue(s, new TypeReference<MapBean>(){});
        System.out.println(mapBean1);

    }
}
