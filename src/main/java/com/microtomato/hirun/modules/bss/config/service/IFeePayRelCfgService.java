package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.FeePayRelCfg;

import java.util.List;

/**
 * 费用项与收款项配置表(FeePayRelCfg)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-05 00:12:28
 */
public interface IFeePayRelCfgService extends IService<FeePayRelCfg> {

    List<FeePayRelCfg> queryAll();

    List<FeePayRelCfg> queryByFeeItemIds(List<Long> feeItemIds);
}