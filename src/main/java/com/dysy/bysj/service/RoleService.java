package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.Role;
import com.dysy.bysj.pojo.PageInfo;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-09
 */
public interface RoleService extends IService<Role> {
    PageInfo getAllRoles(PageInfo pageInfo);

    boolean updateRole(Role role);

    boolean deleteRoleBatch(List<Long> ids);

    boolean deleteRoleById(Long id);
    boolean addRole(Role role);

    boolean updateRoleStatus(Long id, boolean status);
}
