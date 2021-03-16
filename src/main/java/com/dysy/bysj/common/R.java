package com.dysy.bysj.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class R {
    private int code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    private R() {}

    private R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(int code, String message, Map data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 判断接口调用是否成功
     * 注意, boolean 类型的方法在解析为 json 时，会被当成一个字段（在此的字段就是 success）
     * @return
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == 200;
    }

    public static R error(Integer code, String msg) {
        return new R(code, msg);
    }

    public static R error(String msg) {
        return new R(500000, msg);
    }

    public static R ok() {
        return new R(200000, "ok");
    }

    public static R ok(Map<String, Object> data) {
        return new R(200000, "ok", data);
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}