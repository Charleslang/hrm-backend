package com.dysy.bysj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.pojo.PageInfo;
import com.dysy.bysj.service.UnitService;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-17
 */
@RestController
@RequestMapping("/unit")
@CrossOrigin
@Validated
public class UnitController {

    @Autowired
    private UnitService unitService;

    @PostMapping("/unit")
    // 后台必须要使用 @RequesyBody, 不然 PageInfo 里面的 map 无法映射
    public Result getAllUnit(@RequestBody @Valid PageInfo pageInfo) {
        unitService.getAllUnit(pageInfo);
        return Result.ok(pageInfo);
    }

    @DeleteMapping("/{id}")
    public Result deleteUnitById(@PathVariable Long id) {
        unitService.deleteUnitById(id);
        return Result.ok();
    }

    @DeleteMapping("/batch/{ids}")
    public Result deleteUnitByIds(@PathVariable List<Long> ids) {
        unitService.deleteUnitByIds(ids);
        return Result.ok();
    }

    @PutMapping("/st")
    public Result updateUnitStautsById(Long id, boolean status) {
        System.out.println(id + " " + status);
        unitService.updateUnitStatusById(status, id);
        return Result.ok();
    }

}
