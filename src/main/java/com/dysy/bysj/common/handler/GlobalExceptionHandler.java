package com.dysy.bysj.common.handler;

import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.enums.ResponseCodeEnum;
import com.dysy.bysj.common.exception.AuthorizationExpiredException;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.common.exception.ClientException;
import com.dysy.bysj.common.exception.DisabledAccountException;
import com.dysy.bysj.common.exception.IncorrectCredentialsException;
import com.dysy.bysj.common.exception.IncorrectVerfyCodeException;
import com.dysy.bysj.common.exception.LockedAccountException;
import com.dysy.bysj.common.exception.UnkownAccountException;
import com.sun.mail.smtp.SMTPAddressFailedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler({Exception.class})
//    public Result handlerException(Exception e) {
//        e.printStackTrace();
//        return Result.error(ResponseCodeEnum.UNKOWN_ERROR.getCode(),
//                ResponseCodeEnum.UNKOWN_ERROR.getMessage());
//    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handlerException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.MRTHOD_NOT_ALLOWED.getCode(),
                ResponseCodeEnum.MRTHOD_NOT_ALLOWED.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public Result handlerException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(),
                ResponseCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
    }

    // 用户名或密码错误
//    @ExceptionHandler({UnknownAccountException.class, IncorrectCredentialsException.class})
//    public Result handlerException() {
//        return Result.error(ResponseCodeEnum.AUTH_ERROR.getCode(),
//                ResponseCodeEnum.AUTH_ERROR.getMessage());
//    }

    // 没有权限
//    @ExceptionHandler({UnauthorizedException.class})
//    public Result handlerException(UnauthorizedException e) {
//        e.printStackTrace();
//        return Result.error(ResponseCodeEnum.FORBIDDEN.getCode(),
//                ResponseCodeEnum.FORBIDDEN.getMessage());
//    }

    // SpringBoot 2.2.x 中的参数绑定
    @ExceptionHandler({BindException.class})
    public Result handlerException(BindException e) {
        e.popNestedPath();
        return Result.error(ResponseCodeEnum.BAD_REQUEST.getCode(),
                e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // SpringBoot 2.3.x 中参数绑定（传递单个值）
    @ExceptionHandler({ConstraintViolationException.class})
    public Result handlerException(ConstraintViolationException e) {
        e.printStackTrace();
        String[] message = e.getMessage().replaceAll("\\s", "").split(",");
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String s : message) {
            stringJoiner.add(s.split(":")[1]);
        }
        return Result.error(ResponseCodeEnum.BAD_REQUEST.getCode(),
                stringJoiner.toString());
    }

    // SpringBoot 2.3.x 中参数绑定（传递对象）
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result handlerException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage());
//            sb.append('\n');
            sb.append(System.lineSeparator());
        }
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.BAD_REQUEST.getCode(), sb.toString());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result handlerException(MissingServletRequestParameterException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.BAD_REQUEST.getCode(),
                ResponseCodeEnum.BAD_REQUEST.getMessage());
    }

    @ExceptionHandler({NumberFormatException.class})
    public Result handlerException(NumberFormatException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(),
                "无效的整数");
    }

    @ExceptionHandler({BusinessException.class})
    public Result handlerException(BusinessException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.USER_OPERATE_ERROR.getCode(),
                e.getMessage());
    }

    @ExceptionHandler({ClientException.class})
    public Result handlerException(ClientException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.USER_OPERATE_ERROR.getCode(),
                e.getMessage());
    }

    /**
     * 账号不存在
     * @param e
     * @return
     */
    @ExceptionHandler({UnkownAccountException.class})
    public Result handlerException(UnkownAccountException e) {
        return Result.error(ResponseCodeEnum.UNKOWN_ACCOUNT.getCode(),
                ResponseCodeEnum.UNKOWN_ACCOUNT.getMessage());
    }

    /**
     * 密码错误
     * @param e
     * @return
     */
    @ExceptionHandler({IncorrectCredentialsException.class})
    public Result handlerException(IncorrectCredentialsException e) {
        return Result.error(ResponseCodeEnum.INCORRECT_CREDENTIALS.getCode(),
                ResponseCodeEnum.INCORRECT_CREDENTIALS.getMessage());
    }

    /**
     * 账号被禁用
     * @param e
     * @return
     */
    @ExceptionHandler({DisabledAccountException.class})
    public Result handlerException(DisabledAccountException e) {
        return Result.error(ResponseCodeEnum.DISABLED_ACCOUNT.getCode(),
                ResponseCodeEnum.DISABLED_ACCOUNT.getMessage());
    }

    /**
     * 账号被锁定
     * @param e
     * @return
     */
    @ExceptionHandler({LockedAccountException.class})
    public Result handlerException(LockedAccountException e) {
        return Result.error(ResponseCodeEnum.LOCKED_ACCOUNT.getCode(),
                ResponseCodeEnum.LOCKED_ACCOUNT.getMessage());
    }

    /**
     * 验证码错误
     */
    @ExceptionHandler({IncorrectVerfyCodeException.class})
    public Result handlerException(IncorrectVerfyCodeException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.INCORRECT_VERYFY_CODE.getCode(),
                ResponseCodeEnum.INCORRECT_VERYFY_CODE.getMessage());
    }

    /**
     * 发送邮件失败/被拒
     * @param e
     * @return
     */
    @ExceptionHandler({SMTPAddressFailedException.class})
    public Result handlerException(SMTPAddressFailedException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.MAIL_ACCESS_DENY.getCode(),
                ResponseCodeEnum.MAIL_ACCESS_DENY.getMessage());
    }

    @ExceptionHandler({AuthorizationExpiredException.class})
    public Result handlerException(AuthorizationExpiredException e) {
        e.printStackTrace();
        return Result.error(ResponseCodeEnum.AUTHENTICATION_EXPIRE.getCode(),
                e.getMessage());
    }
}
