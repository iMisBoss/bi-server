package com.newtouch.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 字段自动填充处理器
 */
@Slf4j
public class FieldMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("MyBatis-Plus 插入数据，自动填充 createTime, updateTime");

        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());

        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("MyBatis-Plus 更新数据，自动填充 updateTime");

        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
