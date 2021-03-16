package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.common.enums.MenuEnum;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.entity.Menu;
import com.dysy.bysj.mapper.MenuMapper;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.service.RoleAndMenuService;
import com.dysy.bysj.vo.MenuVO;
import com.dysy.bysj.vo.UpdateMenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleAndMenuService roleAndMenuService;

    @Override
    public List<MenuVO> getMenu4User(Long userId) {
        // 查询所有
//        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
//        menuQueryWrapper.orderByAsc("id");
//        List<Menu> menus = menuMapper.selectList(menuQueryWrapper);
        List<Menu> menus = menuMapper.getMenu4User(userId);
        List<MenuVO> menuTree = buildTree(menus);
        return menuTree;
    }

    private MenuVO menu2menuVO(Menu menu) {
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    private List<MenuVO> buildTree(List<Menu> menus) {
        List<MenuVO> menuTree = new ArrayList<>();
        // 先要根据用户拥有的菜单 id 来过滤一次
//        CollectionUtils.contains()
        // 一级菜单
        List<MenuVO> rootMenus = menus.stream().
                filter(e -> e.getParentId() == null || e.getParentId().equals(0))
                .map(this::menu2menuVO)
                .sorted(Comparator.comparing((MenuVO::getSortNo)))
                .collect(Collectors.toList());

        // 构建子菜单
        for (MenuVO rootMenu : rootMenus) {
            rootMenu.setLevel(1);
            menuTree.add(buildSubTree(rootMenu, menus));
        }
        return menuTree;
    }

    private MenuVO buildSubTree(MenuVO parentMenu, List<Menu> menus) {
        List<MenuVO> originalSubMenu = new ArrayList<>();
        for (Menu menu : menus) {
            if (parentMenu.getId().equals(menu.getParentId())) {
                menu.setLevel(parentMenu.getLevel() + 1);
                originalSubMenu.add(buildSubTree(menu2menuVO(menu), menus));
            }
            originalSubMenu.sort(Comparator.comparing(MenuVO::getSortNo));
        }
        parentMenu.setChildren(originalSubMenu);
        return parentMenu;
    }

    @Override
    public boolean deleteMenu(Long id) {
//        List<Long> ids = new ArrayList<>();
//        getAllIds(id, ids);
        List<Long> subMenuIds = menuMapper.getSubMenuIds(id);
        subMenuIds.add(id);
        int delete = menuMapper.deleteBatchIds(subMenuIds);
        if (delete > 0) {
            return true;
        }
        throw new BusinessException("删除菜单信息失败");
    }


    // 查询所有子菜单 id (效率低, 相当于在 for 循环中进行查询)
    private List<Long> getAllIds(Long id, List<Long> ids) {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("parent_id", id).select("id");
        List<Menu> menus = menuMapper.selectList(menuQueryWrapper);
        menus.forEach(e -> {
            ids.add(e.getId());
            getAllIds(e.getId(), ids);
        });
        return ids;
    }

    @Override
    public List<MenuVO> getMenuByRoleId(Long id) {
        List<Menu> menus = menuMapper.getMenuByRoleId(id);
        List<MenuVO> menuTree = buildTree(menus);
        return menuTree;
    }

    @Override
    public List<MenuVO> getAllMenus() {
        List<Menu> menus = menuMapper.selectList(null);
        List<MenuVO> menuList= buildTree(menus);
        return menuList;
    }

    @Transactional
    @Override
    public boolean updateRoleMenu(Long roleId, List<Long> menuIds) {
        // 查询角色原先拥有的菜单
        List<Menu> menus = menuMapper.getMenuByRoleId(roleId);
        // 获取原先拥有的菜单的 id
        List<Long> allMenuIds = menus.stream().map(e -> e.getId()).collect(Collectors.toList());
        // 要添加的新菜单
        List<Long> addIds = menuIds.stream().filter(e -> !allMenuIds.contains(e))
                .collect(Collectors.toList());

        // 原有剩下的菜单
        menuIds.removeAll(addIds);
        // 执行该操作后，allMenuIds 里面就是要删除的菜单
        allMenuIds.removeAll(menuIds);
        boolean addSuccess = true;
        if (!addIds.isEmpty()) {
            addSuccess = roleAndMenuService.addMenu(roleId, addIds);
        }
        boolean deleteSuccess = true;
        if (!menuIds.isEmpty()) {
            deleteSuccess = roleAndMenuService.deleteMenu(roleId, allMenuIds);
        }
        if (addSuccess && deleteSuccess) {
            return true;
        }
        throw new BusinessException("更新角色菜单信息失败");
    }

    @Transactional
    @Override
    public boolean updateMenu(UpdateMenuVO menuVO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVO, menu);
        boolean menuSuccess = updateById(menu);
        boolean subSuccess = true;
        // 如果 menu 被停用, 其子菜单也应该被停用
        if (!menu.getEnable()) {
            List<Long> subMenuIds = menuMapper.getSubMenuIds(menu.getId());
            if (subMenuIds != null && !subMenuIds.isEmpty()) {
                LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(Menu::getId, subMenuIds)
                        .set(Menu::getEnable, MenuEnum.MenuStatusEnum.DISABLE.getKey());
                subSuccess = update(updateWrapper);
            }
        }
        if (menuSuccess && subSuccess) {
            return true;
        }
        throw new BusinessException("更新菜单信息失败");
    }

    @Override
    public boolean updateMenuStatus(Long id, boolean status) {
        List<Long> menuIds = menuMapper.getSubMenuIds(id);
        menuIds.add(id);
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Menu::getId, menuIds).set(Menu::getEnable, status);
        boolean update = update(updateWrapper);
        if (update) {
            return true;
        }
        throw new BusinessException("更新菜单状态失败");
    }
}
