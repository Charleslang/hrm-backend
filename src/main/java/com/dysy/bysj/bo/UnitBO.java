package com.dysy.bysj.bo;

import com.dysy.bysj.vo.UnitVO;
import com.dysy.bysj.vo.UserAdminVO;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-06
 */
public class UnitBO {
    private UnitVO unit;
    private UserAdminVO user;

    public UnitVO getUnit() {
        return unit;
    }

    public void setUnit(UnitVO unit) {
        this.unit = unit;
    }

    public UserAdminVO getUser() {
        return user;
    }

    public void setUser(UserAdminVO user) {
        this.user = user;
    }
}
