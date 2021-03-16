package com.dysy.bysj.controller;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.annotation.RequireAuthentication;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.common.exception.IncorrectVerfyCodeException;
import com.dysy.bysj.service.UserService;
import com.dysy.bysj.util.CommonUtils;
import com.dysy.bysj.util.EncrypUtils;
import com.dysy.bysj.util.RedisUtils;
import com.dysy.bysj.vo.UserVO;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-06
 */
@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EncrypUtils encrypUtils;

    @PutMapping("/pwd")
    public Result resetPassword(@NotBlank(message = "手机号或邮箱不能为空") String account,
                                @NotBlank(message = "验证码不能为空")
                                @Length(max = Constants.APP_VERIFY_CODE_LENGTH,
                                        min = Constants.APP_VERIFY_CODE_LENGTH,
                                        message = "验证码长度应该为{max}位") String code,
                                @NotBlank(message = "密码不能为空") String password) {
        String veryfyCode = (String)redisUtils.get(Constants.VERYFY_CODE_CACHE_PREFIX + account);
        if (Objects.equals(veryfyCode, code)) {
            String salt = CommonUtils.getRandomSequence(Constants.RANDOM_SALT_LENGTH);
            password = encrypUtils.base64Decode(password);
            password = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, password, salt,
                    Constants.APP_SECRET_HASH_ITERATIONS);
            boolean result = userService.resetPassword(account, password, encrypUtils.base64Encode(salt));
            if (result) {
                return Result.ok();
            } else {
                throw new BusinessException("修改密码失败, 请稍后再试");
            }
        }
        throw new IncorrectVerfyCodeException();
    }

    @RequireAuthentication
    @GetMapping("/user")
    public Result getCurrentUser(@RequestHeader(Constants.ACCESS_TOKEN) String token) {
        Object object = redisUtils.get(Constants.REFRESH_TOKEN_CACHE_PREFIX + token);
        UserVO userVO = null;
        if (object instanceof UserVO) {
            userVO = (UserVO)object;
        }
        System.out.println(userVO);
        return Result.ok(userVO);
    }


}
