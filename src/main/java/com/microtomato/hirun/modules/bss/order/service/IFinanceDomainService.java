package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 财务领域服务接口类
 * @author: jinnian
 * @create: 2020-03-01 00:57
 **/
public interface IFinanceDomainService {

    PayComponentDTO initPayComponent(Long orderId, Long payNo);

    CollectionComponentDTO initCollectionComponent(Long payNo);

    void collectFee(CollectFeeDTO feeData);

    void nonCollectFee(NonCollectFeeDTO feeData);

    void nonCollectFeeUpdate(NonCollectFeeDTO feeData);

    void nonCollectFeeForAudit(NonCollectFeeDTO feeData);

    void changePay(CollectFeeDTO feeData);

    IPage<CustOrderInfoDTO> queryCustOrderInfos(CustOrderQueryDTO queryCondition, Page<CustOrderQueryDTO> page);

    List<FinancePendingTaskDTO> queryFinancePendingTask();

    List<OrderPayInfoDTO> queryPayInfoByOrderId(Long orderId);

    List<NonCollectFeeDTO> queryPayInfoByCond(NonCollectFeeQueryDTO queryCondition);

    IPage<FinanceOrderTaskDTO> queryFinanceOrderTasks(FinanceOrderTaskQueryDTO condition);
}
