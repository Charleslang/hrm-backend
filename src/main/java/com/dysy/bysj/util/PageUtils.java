package com.dysy.bysj.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dysy.bysj.pojo.PageInfo;
import org.springframework.stereotype.Component;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-17
 */
@Component
public class PageUtils {

    public PageInfo getPageInfo(Page<?> page, PageInfo pageInfo, boolean transform) {
        pageInfo.setPage(page.getCurrent());
        pageInfo.setSize(page.getSize());
        pageInfo.setPages(page.getPages());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setHasPrev(page.hasPrevious());
        pageInfo.setHasNext(page.hasNext());
        // 是否转为 VO，如果需要，则传入 true
        if (!transform) {
            pageInfo.setData(page.getRecords());
        }
        return  pageInfo;
    }
}
