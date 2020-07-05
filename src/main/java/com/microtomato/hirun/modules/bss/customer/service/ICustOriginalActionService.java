package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.modules.bss.customer.entity.po.CustOriginalAction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户原始动作记录 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
public interface ICustOriginalActionService extends IService<CustOriginalAction> {
    /**
     * 查询家装顾问环节动作完成情况
     * @param customerId
     * @param employeeId
     * @return
     */
    List<CustOriginalAction> queryOriginalInfo(Long customerId, Long employeeId);
}
