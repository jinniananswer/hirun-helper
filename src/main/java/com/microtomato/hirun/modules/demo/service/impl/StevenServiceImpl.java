package com.microtomato.hirun.modules.demo.service.impl;

import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.mapper.StevenMapper;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
public class StevenServiceImpl extends ServiceImpl<StevenMapper, Steven> implements IStevenService {

    @Autowired
    private StevenMapper stevenMapper;

    @Override
    public int insert(Steven steven) {
        return stevenMapper.insert(steven);
    }
}
