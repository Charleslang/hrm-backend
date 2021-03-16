package com.dysy.bysj.controller;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.common.exception.DisabledAccountException;
import com.dysy.bysj.common.exception.IncorrectCredentialsException;
import com.dysy.bysj.common.exception.IncorrectVerfyCodeException;
import com.dysy.bysj.common.exception.LockedAccountException;
import com.dysy.bysj.common.exception.UnkownAccountException;
import com.dysy.bysj.service.LoginService;
import com.dysy.bysj.util.CommonUtils;
import com.dysy.bysj.util.EncrypUtils;
import com.dysy.bysj.util.MailUtils;
import com.dysy.bysj.util.RedisUtils;
import com.dysy.bysj.util.SMSUtils;
import com.dysy.bysj.util.TokenUtils;
import com.dysy.bysj.vo.UserVO;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-30
 */
@RestController
@RequestMapping("/login")
@CrossOrigin
@Validated
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private EncrypUtils encrypUtils;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/ap")
    public Result loginWithAccountAndPwd(@NotBlank(message = "账号不能为空") String account,
                                         @NotBlank(message = "密码不能为空") String password) {
        String salt = loginService.getSalt(account);
        if (StringUtils.isEmpty(salt)) {
            throw new UnkownAccountException();
        }
        password = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, encrypUtils.base64Decode(password),
                encrypUtils.base64Decode(salt), Constants.APP_SECRET_HASH_ITERATIONS);
        UserVO userVO = loginService.getUserByAccountAndPwd(account, password);
        String accessToken = login(userVO);
        HashMap<Object, Object> map = new HashMap<>();
        map.put(Constants.ACCESS_TOKEN, accessToken);
        return Result.ok(map);
    }

    @PostMapping("/code")
    public Result loginWithVerfyCode(@NotBlank(message = "手机号或邮箱不能为空") String account,
                                     @NotBlank(message = "验证码不能为空")
                                     @Length(max = Constants.APP_VERIFY_CODE_LENGTH,
                                             min = Constants.APP_VERIFY_CODE_LENGTH,
                                             message = "验证码长度应该为{max}位") String code) {
        String cacheCode = String.valueOf(redisUtils.get(Constants.VERYFY_CODE_CACHE_PREFIX + account));
        if (Objects.equals(cacheCode, code)) {
            UserVO userVO = loginService.getUserByPhoneOrEmail(account);
            String accessToken = login(userVO);
            HashMap<Object, Object> map = new HashMap<>();
            map.put(Constants.ACCESS_TOKEN, accessToken);
            return Result.ok(map);
        } else {
            throw new IncorrectVerfyCodeException();
        }
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(Constants.ACCESS_TOKEN) String accessToken) {
        tokenUtils.removeToken(Constants.ACCESS_TOKEN_CACHE_PREFIX + accessToken,
                Constants.REFRESH_TOKEN_CACHE_PREFIX + accessToken);
        return Result.ok();
    }

    private void veryfyUser(UserVO userVO) {
        if (Objects.isNull(userVO)) {
            throw new IncorrectCredentialsException();
        }
        if (!userVO.isEnable()) {
            throw new DisabledAccountException();
        }
        if (userVO.isLocked()) {
            throw new LockedAccountException();
        }
    }

    private String login(UserVO userVO) {
        veryfyUser(userVO);
        String accessToken = tokenUtils.generateAccessToken(userVO);
        return accessToken;
    }
}
