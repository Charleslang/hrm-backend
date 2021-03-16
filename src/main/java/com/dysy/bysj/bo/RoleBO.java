package com.dysy.bysj.bo;

import com.dysy.bysj.vo.RoleMenuVO;
import com.dysy.bysj.vo.RoleVO;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-11
 */
public class RoleBO {
    private RoleVO role;
    private List<Long> menuIds;

    public RoleVO getRole() {
        return role;
    }

    public void setRole(RoleVO role) {
        this.role = role;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}
