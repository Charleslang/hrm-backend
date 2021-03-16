package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
public class UnkownAccountException extends BusinessException {

    public UnkownAccountException() {}

    public UnkownAccountException(String message){
        super(message);
    }
}
