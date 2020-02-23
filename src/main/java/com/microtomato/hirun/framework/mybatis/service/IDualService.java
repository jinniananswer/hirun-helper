package com.microtomato.hirun.framework.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.framework.mybatis.po.Dual;

/**
 * @author Steven
 * @date 2020-02-07
 */
public interface IDualService extends IService<Dual> {

    /**
     * 取序列
     *
     * @param cls 序列类，实现序列格式化处理
     * @return
     */
    Long nextval(Class<?> cls);

    /**
     * 取序列
     *
     * @param seqName 序列名
     * @return
     */
    Long nextval(String seqName);

}
