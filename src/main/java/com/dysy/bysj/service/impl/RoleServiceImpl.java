package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.common.enums.RoleEnum;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.entity.Role;
import com.dysy.bysj.mapper.RoleMapper;
import com.dysy.bysj.pojo.PageInfo;
import com.dysy.bysj.service.RoleService;
import com.dysy.bysj.util.JSONUtils;
import com.dysy.bysj.util.PageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public PageInfo getAllRoles(PageInfo pageInfo) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        Page<Role> page = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        Map<String, Object> condition = pageInfo.getCondition();

        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            String name = (String)(condition.get("name"));
            String unitName = (String)(condition.get("unitName"));
            String enable = (String)(condition.get("status"));
            String type = (String)(condition.get("type"));
            List<Long> createTime = new ArrayList<>();
            List<Long> modifyTime = new ArrayList<>();
            try {
                createTime = JSONUtils.obj2list(condition.get("createTime"), Long.class);
                modifyTime = JSONUtils.obj2list(condition.get("modifyTime"), Long.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            queryWrapper.apply(StringUtils.isNotBlank(name), "name = {0}", name);
            queryWrapper.apply(StringUtils.isNotBlank(unitName), "unit_name = {0}", unitName);
            // 拼接 sql 会被先执行, 然后才会执行条件判断; 如果要查询的列不存在, 那么也会被拼接上
            queryWrapper.apply(!"all".equals(enable), "is_enable = {0}", Boolean.valueOf(enable));
            if (!"all".equals(type)) {
                queryWrapper.apply("type = {0}", Integer.valueOf(type));
            }
            if (!createTime.isEmpty()) {
                queryWrapper.apply("(unix_timestamp(date(create_time)) between {0} and {1})",
                        createTime.get(0) / 1000, createTime.get(1) / 1000);
            }
            if (!modifyTime.isEmpty()) {
                queryWrapper.apply(
                        "(unix_timestamp(date(modify_time)) between {0} and {1})",
                        modifyTime.get(0) / 1000, modifyTime.get(1) / 1000);
            }
        }

        Page<Role> rolePage = roleMapper.selectPage(page, queryWrapper);
        // 建议返回 VO
        pageUtils.getPageInfo(rolePage, pageInfo, false);
        return pageInfo;
    }

    @Override
    public boolean updateRole(Role role) {
        int update = roleMapper.updateById(role);
        if (update > 0) {
            return true;
        }
        throw new BusinessException("更新角色信息失败");
    }

    @Override
    public boolean deleteRoleBatch(List<Long> ids) {
        int delete = roleMapper.deleteBatchIds(ids);
        if (delete > 0) {
            return true;
        }
       throw new BusinessException("批量删除失败");
    }

    @Override
    public boolean deleteRoleById(Long id) {
        int delete = roleMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        throw new BusinessException("删除失败");
    }

    @Override
    public boolean addRole(Role role) {
        role.setType(RoleEnum.RoleTypeEnum.CUSTOM.getKey());
        role.setEnable(RoleEnum.RoleStatusEnum.ENABLE.getKey());
        boolean save = save(role);
        if (save) {
            return true;
        }
       throw new BusinessException("添加角色失败");
    }

    @Override
    public boolean updateRoleStatus(Long id, boolean status) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, id).set(Role::isEnable, status);
        boolean update = update(updateWrapper);
        if (update) {
            return true;
        }
        throw new BusinessException("修改角色状态失败");
    }
}
