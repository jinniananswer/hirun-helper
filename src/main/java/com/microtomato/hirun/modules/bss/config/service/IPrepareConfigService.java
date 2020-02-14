package com.microtomato.hirun.modules.bss.config.service;

import com.microtomato.hirun.modules.bss.config.entity.po.PrepareConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
public interface IPrepareConfigService extends IService<PrepareConfig> {
    PrepareConfig queryValid();
}
