package com.dysy.bysj.controller;

import com.dysy.bysj.common.AppContext;
import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.annotation.RequireAuthentication;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.vo.MenuVO;
import com.dysy.bysj.vo.UpdateMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-09
 */
@RestController
@RequestMapping("/menu")
@CrossOrigin
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequireAuthentication
    @GetMapping("/menu")
    public Result getMenu4User() {
        Long id = AppContext.getCurrentUser().getId();
        List<MenuVO> menus = menuService.getMenu4User(id);
        return Result.ok(menus);
    }

    @GetMapping("/menu/{id}")
    public Result getMenuByRoleId(@PathVariable Long id) {
        List<MenuVO> menus= menuService.getMenuByRoleId(id);
        return Result.ok(menus);
    }

    @GetMapping("/all")
    public Result getAllMenus() {
        List<MenuVO> menus= menuService.getAllMenus();
        return Result.ok(menus);
    }

    @PutMapping("/menu")
    public Result updateMenu(@RequestBody UpdateMenuVO menuVO) {
        menuService.updateMenu(menuVO);
        return Result.ok();
    }

    @PutMapping("/status")
    public Result updateMenuStatus(@RequestParam Long id, @RequestParam boolean status) {
        menuService.updateMenuStatus(id, status);
        return Result.ok();
    }

    @DeleteMapping("/menu/{id}")
    public Result deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.ok();
    }
}
