package com.dysy.bysj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dysy.bysj.bo.UnitBO;
import com.dysy.bysj.common.enums.UnitEnum;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.entity.Unit;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.mapper.UnitMapper;
import com.dysy.bysj.mapper.UserMapper;
import com.dysy.bysj.pojo.PageInfo;
import com.dysy.bysj.service.UnitService;
import com.dysy.bysj.service.UserService;
import com.dysy.bysj.util.JSONUtils;
import com.dysy.bysj.util.PageUtils;
import com.dysy.bysj.vo.UnitVO;
import com.dysy.bysj.vo.UserAdminVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-06
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Resource
    private UnitMapper unitMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private PageUtils pageUtils;

    @Override
    public boolean saveSimpleUnit(Unit unit) {
        unit.setType(UnitEnum.UnitTypeEnum.UNIT.getKey());
        unit.setEnable(true);
        boolean result = save(unit);
        return result;
    }

    @Override
    public boolean existUnit(String unitName) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", unitName).select("id");
        Unit unit = getOne(queryWrapper);
        return unit != null;
    }

    @Override
    public PageInfo getAllUnit(PageInfo pageInfo) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_preset", UnitEnum.UnitInnerEnum.ADDITIONAL.getKey());

        Map<String, Object> condition = pageInfo.getCondition();
        if (Objects.nonNull(condition) && !condition.isEmpty()) {
            String name = String.valueOf(condition.get("name"));
            String shortName = String.valueOf(condition.get("shortName"));
            String unitHeader = String.valueOf(condition.get("header"));
            List<Long> createTime = new ArrayList<>();
            try {
                 createTime = JSONUtils.json2list(JSONUtils.obj2json(condition.get("createTime")), Long.class);
            } catch (JsonProcessingException e) {

            }

            List<Long> modifyTime = new ArrayList<>();
            try {
                modifyTime = JSONUtils.json2list(JSONUtils.obj2json(condition.get("modifyTime")), Long.class);
            } catch (JsonProcessingException e) {

            }
            String enable = String.valueOf(condition.get("status"));
            queryWrapper.apply(StringUtils.isNotBlank(name), "name = {0}", name);
            queryWrapper.apply(StringUtils.isNotBlank(shortName), "short_name = {0}", shortName);
            queryWrapper.apply(StringUtils.isNotBlank(unitHeader), "unit_header = {0}", unitHeader);

            if (!createTime.isEmpty()) {
                queryWrapper.apply("(unix_timestamp(date(create_time)) between {0} and {1})",
                        createTime.get(0) / 1000, createTime.get(1) / 1000);
            }
            if (!modifyTime.isEmpty()) {
                queryWrapper.apply(
                        "(unix_timestamp(date(modify_time)) between {0} and {1})",
                        modifyTime.get(0) / 1000, modifyTime.get(1) / 1000);
            }
            if (!"all".equals(enable)) {
                queryWrapper.apply("is_enable = {0}", Boolean.valueOf(enable));
            }

        }


        Page<Unit> unitPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        Page<Unit> resultPage = unitMapper.selectPage(unitPage, queryWrapper);

        pageUtils.getPageInfo(resultPage, pageInfo, true);
        List<Unit> units = resultPage.getRecords();
        List<UnitVO> unitList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Unit unit : units) {
            UnitVO unitVO = new UnitVO();
            BeanUtils.copyProperties(unit, unitVO);
            unitList.add(unitVO);
            ids.add(unit.getUnitHeader());
        }

        List<UnitBO> resultList = new ArrayList<>();
        // 如果 ids 为空, 使用 in 查询会报错
        if (!ids.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(ids);
            for (UnitVO unitVO : unitList) {
                for (User user : users) {
                    if (user.getId().equals(unitVO.getUnitHeader())) {
                        UserAdminVO userAdminVO = new UserAdminVO();
                        BeanUtils.copyProperties(user, userAdminVO);
                        UnitBO unitBO = new UnitBO();
                        unitBO.setUnit(unitVO);
                        unitBO.setUser(userAdminVO);
                        resultList.add(unitBO);
                        break;
                    }
                }
            }
        }

        pageInfo.setData(resultList);
        return pageInfo;
    }

    @Override
    public boolean updateUnit(Unit unit) {
        int result = unitMapper.updateById(unit);
        return result > 0;
    }

    @Override
    public boolean deleteUnitById(Long id) {
        int delete = unitMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        throw new BusinessException("删除失败");
    }

    @Override
    public boolean deleteUnitByIds(List<Long> ids) {
        int delete = unitMapper.deleteBatchIds(ids);
        if (delete > 0) {
            return true;
        }
        throw new BusinessException("批量删除失败");
    }

    @Override
    public boolean updateUnitStatusById(boolean status, Long id) {
        LambdaUpdateWrapper<Unit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Unit::getId, id).set(Unit::isEnable, status);
        int update = unitMapper.update(null, updateWrapper);
        if (update > 0) {
            return true;
        }
        throw new BusinessException("更新状态失败");
    }

    @Override
    public PageInfo getUnitByCondition(PageInfo pageInfo) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_preset", UnitEnum.UnitInnerEnum.ADDITIONAL.getKey());
        Page<Unit> unitPage = new Page<>(pageInfo.getPage(), pageInfo.getSize());
        Page<Unit> resultPage = unitMapper.selectPage(unitPage, queryWrapper);

        pageUtils.getPageInfo(resultPage, pageInfo, true);
        List<Unit> units = resultPage.getRecords();
        List<UnitVO> unitList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Unit unit : units) {
            UnitVO unitVO = new UnitVO();
            BeanUtils.copyProperties(unit, unitVO);
            unitList.add(unitVO);
            ids.add(unit.getUnitHeader());
        }
        List<User> users = userMapper.selectBatchIds(ids);
        List<UnitBO> resultList = new ArrayList<>();
        for (UnitVO unitVO : unitList) {
            for (User user : users) {
                if (user.getId().equals(unitVO.getUnitHeader())) {
                    UserAdminVO userAdminVO = new UserAdminVO();
                    BeanUtils.copyProperties(user, userAdminVO);
                    UnitBO unitBO = new UnitBO();
                    unitBO.setUnit(unitVO);
                    unitBO.setUser(userAdminVO);
                    resultList.add(unitBO);
                    break;
                }
            }
        }
        pageInfo.setData(resultList);
        return pageInfo;
    }
}
