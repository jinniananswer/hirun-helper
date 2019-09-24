package com.microtomato.hirun.framework.aop;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 实现公共字段自动设置
 *
 * @author Steven
 * @date 2019-04-28
 */
@Component
public class AutoSetMetaObjectAdvice implements MetaObjectHandler {

    private static final String GMT_CREATE = "gmtCreate";
    private static final String GMT_MODIFIED = "gmtModified";

    /**
     * 在新增记录时自动设置。
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.setInsertFieldValByName(GMT_CREATE, localDateTime, metaObject);
        this.setInsertFieldValByName(GMT_MODIFIED, localDateTime, metaObject);

    }

    /**
     * 在修改记录时自动设置
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName(GMT_MODIFIED, LocalDateTime.now(), metaObject);
    }
}
