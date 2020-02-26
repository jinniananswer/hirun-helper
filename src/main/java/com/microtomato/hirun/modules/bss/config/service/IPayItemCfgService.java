package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;

import java.util.List;

/**
 * <p>
 * 收款项配置表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-23
 */
public interface IPayItemCfgService extends IService<PayItemCfg> {

    List<PayItemCfg> queryAll();

    PayItemCfg getPayItem(Long payItemId);

    List<PayItemCfg> queryPlusPayItems();
}
