package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dysy.bysj.bo.RoleBO;
import com.dysy.bysj.bo.UnitBO;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.common.exception.ClientException;
import com.dysy.bysj.entity.Menu;
import com.dysy.bysj.entity.Role;
import com.dysy.bysj.entity.Unit;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.service.BusinessService;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.service.RoleAndMenuService;
import com.dysy.bysj.service.RoleService;
import com.dysy.bysj.service.UnitService;
import com.dysy.bysj.service.UserService;
import com.dysy.bysj.vo.RoleMenuVO;
import com.dysy.bysj.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-07
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private UnitService unitService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleAndMenuService roleAndMenuService;

    @Transactional
    @Override
    public boolean saveSimpleUser(Unit unit, User user) {
        boolean unitResult = unitService.saveSimpleUnit(unit);
        user.setUnitId(unit.getId());
        boolean userResult = userService.save(user);
        return unitResult && userResult;
    }

    @Override
    public boolean existUnitOrUser(String unitName, String account) {
        boolean existUnit = unitService.existUnit(unitName);
        if (existUnit) {
            throw new ClientException("公司名称已被注册");
        }
        boolean existUser = userService.existUser(account);
        if (existUser) {
            throw new ClientException("手机号已被注册");
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateUnitInfo(UnitBO unitBO) {
        Unit unit = new Unit();
        BeanUtils.copyProperties(unitBO.getUnit(), unit);
        unit.setCreateTime(null);
        unit.setModifyTime(null);
        User user = new User();
        BeanUtils.copyProperties(unitBO.getUser(), user);
        user.setCreateTime(null);
        user.setModifyTime(null);

        boolean res1 = unitService.updateUnit(unit);
        boolean res2 = userService.updateUser(user);
        return res1 && res2;
    }

    @Transactional
    @Override
    public boolean addUnitAndUser(UnitBO unitBO) {
        Unit unit = new Unit();
        BeanUtils.copyProperties(unitBO.getUnit(), unit);
        User user = new User();
        BeanUtils.copyProperties(unitBO.getUser(), user);
        boolean res1 = unitService.saveSimpleUnit(unit);
        user.setUnitId(unit.getId());
        boolean res2 = userService.saveSimpleUser(user);
        return res1 && res2;
    }

    @Transactional
    @Override
    public boolean updateRoleInfo(RoleBO roleBO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleBO.getRole(), role);
        boolean roleSuccess = roleService.updateRole(role);
        boolean menuSuccess = menuService.updateRoleMenu(role.getId(), roleBO.getMenuIds());
        return roleSuccess && menuSuccess;
    }

    @Transactional
    @Override
    public boolean addRoleMenu(RoleBO roleBO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleBO.getRole(), role);
        boolean roleSuccess = roleService.addRole(role);
        boolean menuSuccess = roleAndMenuService.addMenu(role.getId(), roleBO.getMenuIds());
        return roleSuccess && menuSuccess;
    }
}
