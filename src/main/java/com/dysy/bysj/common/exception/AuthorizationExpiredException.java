package com.dysy.bysj.common.exception;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
public class AuthorizationExpiredException extends BusinessException {
    public AuthorizationExpiredException() {}
    public AuthorizationExpiredException(String message) {
        super(message);
    }
}
