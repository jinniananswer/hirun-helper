package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;

import java.util.List;

/**
 * 费用项配置表(FeeItemCfg)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 22:22:39
 */
public interface IFeeItemCfgService extends IService<FeeItemCfg> {

    List<FeeItemCfg> queryAll();

    FeeItemCfg getFeeItem(Long feeItemId);

    List<FeeItemCfg> queryLeafByType(String type);
}