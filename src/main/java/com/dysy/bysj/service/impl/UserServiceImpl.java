package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.common.Constants;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.mapper.UserMapper;
import com.dysy.bysj.service.UserService;
import com.dysy.bysj.util.CommonUtils;
import com.dysy.bysj.util.EncrypUtils;
import com.dysy.bysj.vo.UserAdminVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private EncrypUtils encrypUtils;

    @Override
    public boolean existUser(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("telephone", account).or().eq("email", account);
        User user = this.getOne(queryWrapper);
        return user != null;
    }

    @Override
    public boolean saveSimpleUser(User user) {
        String pwd = encrypUtils.base64Decode(user.getPassword());
        String salt = CommonUtils.getRandomSequence(Constants.RANDOM_SALT_LENGTH);
        String password = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, pwd, salt,
                Constants.APP_SECRET_HASH_ITERATIONS);
        user.setPassword(password);
        user.setSalt(encrypUtils.base64Encode(salt));
        user.setEnable(true);
        boolean result = save(user);
        return result;
    }

    @Override
    public boolean resetPassword(String account, String password, String salt) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("telephone", account).or().eq("email", account)
                .set("password", password).set("salt", salt);
        boolean result = this.update(updateWrapper);
        return result;
    }

    @Override
    public UserAdminVO getUnitAdmin(Long userId) {
        User user = userMapper.selectById(userId);
        UserAdminVO userAdminVO = new UserAdminVO();
        BeanUtils.copyProperties(user, userAdminVO);
        return userAdminVO;
    }

    @Override
    public boolean updateUser(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", user.getId()).select("password");
        User one = this.getOne(userQueryWrapper);
        user.setPassword(encrypUtils.base64Decode(user.getPassword()));
        if (one.getPassword().equals(user.getPassword())) {
            user.setPassword(null);
            System.out.println("密码未修改");
        } else {
            String salt = CommonUtils.getRandomSequence(Constants.RANDOM_SALT_LENGTH);
            String password = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, user.getPassword(), salt,
                    Constants.APP_SECRET_HASH_ITERATIONS);
            user.setPassword(password);
            user.setSalt(encrypUtils.base64Encode(salt));
            System.out.println("新密码：" + password);
            System.out.println("未加密盐: " + salt);
        }
        int result = userMapper.updateById(user);
        return result > 0;
    }
}
