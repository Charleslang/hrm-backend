package com.dysy.bysj.mp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        long timeMillis = System.currentTimeMillis();
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
        this.strictInsertFill(metaObject, "enable", Boolean.class, true);
        this.strictInsertFill(metaObject, "createTime", Timestamp.class, new Timestamp(timeMillis));
        this.strictInsertFill(metaObject, "modifyTime", Timestamp.class, new Timestamp(timeMillis));
        System.out.println("插入自动填充");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("更新自动填充");
        this.strictInsertFill(metaObject, "modifyTime", Timestamp.class, new Timestamp(System.currentTimeMillis()));
    }
}
