package com.dysy.bysj.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-08
 */
public class JSONUtils {

    public static String obj2json(Object object) throws JsonProcessingException{
        if (object == null || "".equals(String.valueOf(object))) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        return jsonString;
    }

    public static <T> List<T> json2list(String json, Class<T> clazz) throws JsonProcessingException{
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        List<T> list = mapper.readValue(json, javaType);
        return list;
    }

    public static <T> List<T> obj2list(Object obj, Class<T> clazz) throws JsonProcessingException {
        String json = obj2json(obj);
        List<T> result = json2list(json, clazz);
        return result;
    }

}
