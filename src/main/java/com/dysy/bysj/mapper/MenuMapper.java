package com.dysy.bysj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dysy.bysj.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenu4User(@Param("userId") Long userId);
    List<Menu> getMenuByRoleId(Long id);
    List<Long> getSubMenuIds(Long id);
}
