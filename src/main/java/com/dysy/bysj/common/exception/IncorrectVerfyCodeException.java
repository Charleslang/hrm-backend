package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
public class IncorrectVerfyCodeException extends BusinessException {
    public IncorrectVerfyCodeException() {}
    public IncorrectVerfyCodeException(String message) {
        super(message);
    }
}
