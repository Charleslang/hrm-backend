package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.mapper.RoleAndMenuMapper;
import com.dysy.bysj.entity.RoleAndMenu;
import com.dysy.bysj.service.RoleAndMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-11
 */
@Service
public class RoleAndMenuServiceImpl extends ServiceImpl<RoleAndMenuMapper, RoleAndMenu> implements RoleAndMenuService {

    @Resource
    private RoleAndMenuMapper mapper;

    @Override
    public boolean addMenu(Long roleId, List<Long> menuIds) {
        List<RoleAndMenu> rmList = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleAndMenu roleAndMenu = new RoleAndMenu();
            roleAndMenu.setRoleId(roleId);
            roleAndMenu.setMenuId(menuId);
            rmList.add(roleAndMenu);
        }
        boolean insert = saveBatch(rmList);
        if (insert) {
            return true;
        }
        throw new BusinessException("添加角色菜单错误");
    }

    @Override
    public boolean deleteMenu(Long roleId, List<Long> menuIds) {
        LambdaUpdateWrapper<RoleAndMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleAndMenu::getRoleId, roleId).in(RoleAndMenu::getMenuId, menuIds)
                .set(RoleAndMenu::isDeleted, 1);
        boolean update = update(wrapper);
        if (update) {
            return true;
        }
        throw new BusinessException("删除角色菜单错误");
    }
}
