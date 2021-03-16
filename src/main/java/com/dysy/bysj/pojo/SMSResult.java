package com.dysy.bysj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author: Dai Junfeng
 * @create: 2020-12-26
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class SMSResult {
    private String code;
    private String message;
    private String requestid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestId) {
        this.requestid = requestId;
    }

    @Override
    public String toString() {
        return "SMSBean{" +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestId='" + requestid + '\'' +
                '}';
    }
}
