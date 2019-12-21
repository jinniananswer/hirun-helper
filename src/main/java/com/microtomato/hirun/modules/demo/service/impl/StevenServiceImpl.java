package com.microtomato.hirun.modules.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.mapper.StevenMapper;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
@Slf4j
@Service
@DataSource(DataSourceKey.SYS)
public class StevenServiceImpl extends ServiceImpl<StevenMapper, Steven> implements IStevenService {

    @Autowired
    private StevenMapper stevenMapper;

    @Override
    public int insert(Steven steven) {
        return stevenMapper.insert(steven);
    }
}
