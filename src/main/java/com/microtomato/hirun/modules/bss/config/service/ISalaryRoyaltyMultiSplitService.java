package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyMultiSplit;

/**
 * 员工工资提成多人拆分比例配置(SalaryRoyaltyMultiSplit)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-26 20:56:16
 */
public interface ISalaryRoyaltyMultiSplitService extends IService<SalaryRoyaltyMultiSplit> {

    SalaryRoyaltyMultiSplit getMultiSplit(String type, String item, Integer num);
}