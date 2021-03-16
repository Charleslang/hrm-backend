package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.mapper.LoginMapper;
import com.dysy.bysj.service.LoginService;
import com.dysy.bysj.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    @Resource
    private LoginMapper loginMapper;

    @Override
    public String getSalt(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", account).or().eq("telephone", account).or()
                .eq("email", account).select("salt");
        User user = this.getOne(queryWrapper);
        if (Objects.nonNull(user)) {
            return user.getSalt();
        }
        return "";
    }

    @Override
    public UserVO getUserByAccountAndPwd(String account, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("password", password)
                .and(i -> i.eq("username", account).or().eq("telephone", account).or()
                        .eq("email", account));
        User user = loginMapper.selectOne(queryWrapper);
        if (Objects.nonNull(user)) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }
        return null;
    }

    @Override
    public UserVO getUserByPhoneOrEmail(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telephone", account).or().eq("email", account);
        User user = loginMapper.selectOne(queryWrapper);
        if (Objects.nonNull(user)) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }
        return null;
    }
}
