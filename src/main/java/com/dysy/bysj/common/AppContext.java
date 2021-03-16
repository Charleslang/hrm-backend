package com.dysy.bysj.common;

import com.dysy.bysj.vo.UserVO;

public class AppContext {

    private static final ThreadLocal<UserVO> currUser = new ThreadLocal<>();

    public static void setCurrentUser(UserVO userVO){
        currUser.set(userVO);
    }

    public static UserVO getCurrentUser(){
        return currUser.get();
    }

    public static void removeCurrentUser(){
        currUser.remove();
    }
}
