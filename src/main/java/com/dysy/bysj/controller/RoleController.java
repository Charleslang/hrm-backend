package com.dysy.bysj.controller;

import com.dysy.bysj.common.Result;
import com.dysy.bysj.pojo.PageInfo;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.service.RoleService;
import com.dysy.bysj.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-03-10
 */
@RequestMapping("/role")
@RestController
@CrossOrigin
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/roles")
    public Result getAllRole(@RequestBody PageInfo pageInfo) {
        PageInfo allRoles = roleService.getAllRoles(pageInfo);
        return Result.ok(allRoles);
    }

    @DeleteMapping("/batch/{ids}")
    public Result deleteRoleBatch(@PathVariable List<Long> ids) {
        roleService.deleteRoleBatch(ids);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result deleteRoleById(@PathVariable Long id) {
        roleService.deleteRoleById(id);
        return Result.ok();
    }

    @PutMapping("/{id}/{status}")
    public Result updateRoleStatus(@PathVariable Long id, @PathVariable boolean status) {
        roleService.updateRoleStatus(id, status);
        return Result.ok();
    }


}
