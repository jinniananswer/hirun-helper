package com.microtomato.hirun.framework.mybatis.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;

/**
 *  Mybatis 的 Configuration 可重用版本
 *
 * @author Steven
 * @date 2020-09-17
 */
public class ReusableConfiguration extends MybatisConfiguration implements Cloneable {

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
