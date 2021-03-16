package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.Menu;
import com.dysy.bysj.vo.MenuVO;
import com.dysy.bysj.vo.UpdateMenuVO;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取用户拥有的所有菜单
     * @param userId 用户 id
     * @return
     */
    List<MenuVO> getMenu4User(Long userId);


    boolean deleteMenu(Long id);

    /**
     * 得到角色拥有的菜单
     * @param id 角色 id
     * @return
     */
    List<MenuVO> getMenuByRoleId(Long id);

    /**
     * 查询所有菜单
     * @return
     */
    List<MenuVO> getAllMenus();

    /**
     * 更新角色对应的菜单信息
     * @param roleId
     * @param menuIds
     * @return
     */
    boolean updateRoleMenu(Long roleId, List<Long> menuIds);

    boolean updateMenu(UpdateMenuVO menuVO);

    boolean updateMenuStatus(Long id, boolean status);
}
