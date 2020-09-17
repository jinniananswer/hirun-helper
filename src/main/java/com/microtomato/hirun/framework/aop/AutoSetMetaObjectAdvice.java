package com.microtomato.hirun.framework.aop;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.framework.util.WebContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 实现公共字段自动设置
 *
 * @author Steven
 * @date 2019-04-28
 */
@Slf4j
@Component
public class AutoSetMetaObjectAdvice implements MetaObjectHandler {

    /**
     * 记录创建时间
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 记录更新时间
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 记录创建工号
     */
    private static final String CREATE_USER_ID = "createUserId";

    /**
     * 记录更新工号
     */
    private static final String UPDATE_USER_ID = "updateUserId";

    /**
     * 在新增记录时自动设置。
     *
     * @param metaObject 源对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        Long userId = getUserId();
        LocalDateTime requestTime = getRequestTime();

        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, requestTime);
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, requestTime);
        this.strictInsertFill(metaObject, CREATE_USER_ID, Long.class, userId);
        this.strictInsertFill(metaObject, UPDATE_USER_ID, Long.class, userId);

    }

    /**
     * 在修改记录时自动设置
     *
     * @param metaObject 源对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = getUserId();
        LocalDateTime requestTime = getRequestTime();

        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, requestTime);
        this.strictUpdateFill(metaObject, UPDATE_USER_ID, Long.class, userId);
    }

    private Long getUserId() {
        Long userId = null;
        try {
            userId = WebContextUtils.getUserContext().getUserId();
        } catch(Exception e) {
            userId = Constants.DEFAULT_USER_ID;
        }
        return userId;
    }

    private LocalDateTime getRequestTime() {
        LocalDateTime requestTime = RequestTimeHolder.getRequestTime();
        if (null == requestTime) {
            requestTime = LocalDateTime.now();
        }
        return requestTime;
    }
}
