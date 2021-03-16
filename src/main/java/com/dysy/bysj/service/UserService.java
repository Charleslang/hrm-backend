package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.vo.UserAdminVO;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-06
 */
public interface UserService extends IService<User> {

    boolean resetPassword(String account, String password, String salt);
    boolean existUser(String account);
    boolean saveSimpleUser(User user);
    UserAdminVO getUnitAdmin(Long userId);

    boolean updateUser(User user);
}
