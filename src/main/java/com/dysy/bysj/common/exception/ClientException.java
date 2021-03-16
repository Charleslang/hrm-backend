package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
public class ClientException extends RuntimeException {

    public ClientException() {}

    public ClientException(String message) {
        super(message);
    }
}
