package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.RoleAndMenu;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-11
 */
public interface RoleAndMenuService extends IService<RoleAndMenu> {
    boolean addMenu(Long roleId, List<Long> menuIds);
    boolean deleteMenu(Long roleId, List<Long> menuIds);
}
