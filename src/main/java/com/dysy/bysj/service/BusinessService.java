package com.dysy.bysj.service;

import com.dysy.bysj.bo.RoleBO;
import com.dysy.bysj.bo.UnitBO;
import com.dysy.bysj.entity.Unit;
import com.dysy.bysj.entity.User;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
public interface BusinessService {
    boolean saveSimpleUser(Unit unit, User user);
    boolean existUnitOrUser(String unitName, String account);
    boolean updateUnitInfo(UnitBO unitBO);
    boolean addUnitAndUser(UnitBO unitBO);
    boolean updateRoleInfo(RoleBO roleBO);

    boolean addRoleMenu(RoleBO roleBO);
}
