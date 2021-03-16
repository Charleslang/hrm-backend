package com.dysy.bysj.common.enums;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
public enum ResponseCodeEnum {
    // HttpStatus.OK
    OK(200, "ok"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "请求未找到"),
    MRTHOD_NOT_ALLOWED(405, "请求方式错误"),

    INTERNAL_SERVER_ERROR(500, "服务器出错"),

//    AUTHENTICATION_ERROR(1000, "用户名或密码错误"),
    USER_OPERATE_ERROR(1000, "用户操作错误"),
    UNKOWN_ACCOUNT(1001, "账号不存在"),
    DISABLED_ACCOUNT(1002, "账号未启用"),
    LOCKED_ACCOUNT(1002, "账户已被锁定"),
    AUTHENTICATION_EXPIRE(1003, "登录已失效"),
    EXISTED_USER(1004, "该用户已存在"),
    INCORRECT_CREDENTIALS(1005, "密码错误"),
    INCORRECT_VERYFY_CODE(1006, "验证码错误"),

    MAIL_ACCESS_DENY(1100, "邮箱未找到或访问被拒绝"),

    UNKOWN_ERROR(9000, "未知异常发生")
    ;

    private int code;
    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
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
}
