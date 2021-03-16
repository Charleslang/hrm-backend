package com.dysy.bysj.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private int code;
    private String message;
    private T data;

    private Result() {}

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(int code, String message, T data) {
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

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result error(String msg) {
        return new Result(500, msg);
    }

    public static Result ok() {
        return new Result(200, "ok");
    }

    public static <T> Result ok(T data) {
        return new Result(200, "ok", data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
