package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @create: 2021-01-27
 **/
public class BusinessException extends RuntimeException {

    public BusinessException() {}

    public BusinessException(String msg) {
        super(msg);
    }
}
