package com.dysy.bysj.util;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.exception.AuthorizationExpiredException;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author: Dai Junfeng
 * @create: 2021-01-27
 **/
@Component
public class TokenUtils {

    @Autowired
    private RedisUtils redisUtils;

    public String createToken() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生成 token
     * @param userVO
     * @return
     */
    public String generateAccessToken(UserVO userVO) {
        String accessToken = createToken();
        boolean res1 = redisUtils.set(Constants.ACCESS_TOKEN_CACHE_PREFIX + accessToken,
                userVO, Constants.ACCESS_TOKEN_EXPIRE_TIME);
        boolean res2 = redisUtils.set(Constants.REFRESH_TOKEN_CACHE_PREFIX + accessToken,
                userVO, Constants.REFRESH_TOKEN_EXPIRE_TIME);
        if (!(res1 && res2)) {
            throw new BusinessException("accessToken 保存出错");
        }
        return accessToken;
    }

    /**
     * 刷新 token
     * @param refreshToken
     * @param userVO
     * @return
     */
    public String refreshToken(String refreshToken, UserVO userVO) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException("token 为空");
        }
        if (redisUtils.hasKey(Constants.REFRESH_TOKEN_CACHE_PREFIX + refreshToken)) {
            removeToken(Constants.REFRESH_TOKEN_CACHE_PREFIX + refreshToken);
            String accessToken = generateAccessToken(userVO);
            return accessToken;
        }
        throw new AuthorizationExpiredException("登录已过期, 请重新登录");
    }

    /**
     * 校验 token
     * @param accessToken
     * @return
     */
    public boolean isValidatedToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        }
        if (redisUtils.hasKey(Constants.ACCESS_TOKEN_CACHE_PREFIX + accessToken)) {
//            long expire = redisUtils.getExpire(Constants.ACCESS_TOKEN_CACHE_PREFIX + accessToken);
//            if (expire <= 0) {
//                throw new BusinessException("accessToken 过期");
//            }
            return true;
        } else {
            throw new AuthorizationExpiredException("登录状态已过期");
        }
    }

    /**
     * 移除 token
     * @param token
     */
    public void removeToken(String... token) {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("token 为空, 移除失败");
        }
        redisUtils.del(token);
    }

    public UserVO getUserVO(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("token 为空");
        }
        Object obj = redisUtils.get(token);
//        if (obj == null) {
//            return null;
//        }
        UserVO userVo = null;
        if (obj instanceof UserVO) {
            userVo = (UserVO)obj;
        }
        return userVo;
    }
}
