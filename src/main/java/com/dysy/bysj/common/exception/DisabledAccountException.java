package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
public class DisabledAccountException extends BusinessException {

    public DisabledAccountException() {}

    public DisabledAccountException(String message) {
        super(message);
    }
}
