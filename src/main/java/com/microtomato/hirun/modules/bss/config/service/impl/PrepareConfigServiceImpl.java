package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.config.entity.po.PrepareConfig;
import com.microtomato.hirun.modules.bss.config.mapper.PrepareConfigMapper;
import com.microtomato.hirun.modules.bss.config.service.IPrepareConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
@Slf4j
@Service
public class PrepareConfigServiceImpl extends ServiceImpl<PrepareConfigMapper, PrepareConfig> implements IPrepareConfigService {

    @Override
    public PrepareConfig queryValid() {
        return this.baseMapper.selectOne(new QueryWrapper<PrepareConfig>().lambda().eq(PrepareConfig::getStatus,"1"));
    }
}
