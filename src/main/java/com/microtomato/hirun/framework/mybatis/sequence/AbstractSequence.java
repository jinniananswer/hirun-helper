package com.microtomato.hirun.framework.mybatis.sequence;

import com.microtomato.hirun.framework.mybatis.service.IDualService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 序列抽象类，供业务继承实现
 *
 * @author Steven
 * @date 2020-02-10
 */
public abstract class AbstractSequence implements ISequence {

    @Autowired
    private IDualService dualService;

    protected Long nextval(String sequenceName) {
        return dualService.nextval(sequenceName);
    }

}
