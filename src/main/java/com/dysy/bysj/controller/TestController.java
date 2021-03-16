package com.dysy.bysj.controller;

import com.dysy.bysj.common.Result;
import com.dysy.bysj.entity.Menu;
import com.dysy.bysj.mapper.MenuMapper;
import com.dysy.bysj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    @Autowired
    private MenuService menuService;

    @Resource
    private MenuMapper menuMapper;

    @RequestMapping("/menu")
    public Result testSelect() {
        Menu menu = menuMapper.selectById(1);
        return Result.ok(menu);
    }

    @RequestMapping("/insert")
    public Result testInsert() {
        long time = System.currentTimeMillis();
        Menu menu = new Menu("组织机构图", null, 1L, "/tu", "Tu", 3, false, true,
                new Timestamp(time), null, new Timestamp(time), null, null);
        int insert = menuMapper.insert(menu);
        System.out.println(menu.getId());
        return Result.ok();
    }

    @RequestMapping("/insert1")
    public Result testInsertWithAutoFill() {
        long time = System.currentTimeMillis();
        Menu menu = new Menu("组织机构图1", null, 1L, "/tu", "Tu", 3,
                null, null, null, null, null);
        int insert = menuMapper.insert(menu);
        System.out.println(menu.getId());
        return Result.ok();
    }


    @RequestMapping("/update")
    public Result update() {
        Menu menu = new Menu(6L,"组织机构图123", null, 1L, "/tu", "Tu", 3, false, true,
                null, null, null, null, null);
        int update = menuMapper.updateById(menu);
        return Result.ok();
    }

    @RequestMapping("/delete")
    public Result delete() {
        int delete = menuMapper.deleteById(6L);
        return Result.ok();
    }

    @RequestMapping("/g1")
    public Result getAllMenu() {
//        List<Menu> allMenu = menuService.getAllMenu();
//        return Result.ok(allMenu);
        return null;
    }

    @RequestMapping("/d1")
    public Result deleteMenu() {
        boolean deleteMenu = menuService.deleteMenu(1000L);
        return Result.ok(deleteMenu);
    }
}
