package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
public class IncorrectCredentialsException extends BusinessException {

    public IncorrectCredentialsException() {}

    public IncorrectCredentialsException(String message) {
        super(message);
    }
}
