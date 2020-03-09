package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.WoodContractDTO;


/**
 * @program: hirun-helper
 * @description: 分期收取工程款领域服务接口类
 * @author: liuhui
 * @create: 2020-03-05 21:10
 **/
public interface IInstallmentCollectDomainService {
    /**
     * 保存尾款收取信息
     * @param dto
     */
    void saveLastCollectionFee(LastInstallmentCollectionDTO dto);

    /**
     * 查询尾款信息
     * @param orderId
     * @return
     */
    LastInstallmentCollectionDTO queryLastInstallmentCollect(Long orderId);

    void applyFinanceAuditLast(LastInstallmentCollectionDTO dto);

    void submitWoodContract(WoodContractDTO dto);

    void auditWoodFirstCollect(WoodContractDTO dto);

    void submitWoodLastCollect(WoodContractDTO dto);

    void auditWoodLastCollect(WoodContractDTO dto);
}
