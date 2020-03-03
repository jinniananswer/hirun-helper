package com.microtomato.hirun.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * Skeleton 服务基类，业务原子服务继承于它
 *
 * @author Steven
 * @date 2020-03-03
 */
public class CoreServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M,T> {

    @Override
    public boolean save(T entity) {
        return super.save(entity);
    }

}
