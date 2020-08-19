package com.test.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapBean {


    public MapBean() {
    }

    public MapBean(String json) {
        this.map = new LinkedHashMap<>();
    }

    //@JsonSerialize(using = MySerializer.class)
    @JsonDeserialize(using = MyDeserialize.class)
    private Map<List<String>, Object> map = new LinkedHashMap<List<String>, Object>();

    public Map<List<String>, Object> getMap() {
        return map;
    }

    public void setMap(Map<List<String>, Object> map) {
        this.map = map;
    }
}
