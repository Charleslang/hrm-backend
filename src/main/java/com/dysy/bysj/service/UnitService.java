package com.dysy.bysj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dysy.bysj.entity.Unit;
import com.dysy.bysj.pojo.PageInfo;
import com.dysy.bysj.vo.UnitVO;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-06
 */
public interface UnitService extends IService<Unit> {

    /**
     * 注册
     * @param unit
     * @return true: 注册成功
     */
    boolean saveSimpleUnit(Unit unit);

    /**
     * 是否存在单位
     * @param unitName
     * @return true: 存在
     */
    boolean existUnit(String unitName);

    /**
     * 得到所有单位
     * @param pageInfo
     * @return
     */
    PageInfo getAllUnit(PageInfo pageInfo);

    boolean updateUnit(Unit unit);

    boolean deleteUnitById(Long id);

    boolean deleteUnitByIds(List<Long> ids);

    boolean updateUnitStatusById(boolean status, Long id);

    PageInfo getUnitByCondition(PageInfo pageInfo);

}
