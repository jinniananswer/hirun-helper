package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemStageCfg;

/**
 * 费用项分期配置表(FeeItemStageCfg)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-08 00:43:22
 */
public interface IFeeItemStageCfgService extends IService<FeeItemStageCfg> {

    FeeItemStageCfg getByFeeItemIdTypePeriod(Long feeItemId, String type, Integer period);
}