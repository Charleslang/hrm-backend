package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.vo.UserVO;

/**
 * @author: Dai Junfeng
 * @date: 2021-02-01
 */
public interface LoginService extends IService<User> {

    String getSalt(String account);
    UserVO getUserByAccountAndPwd(String account, String password);
    UserVO getUserByPhoneOrEmail(String account);

}
