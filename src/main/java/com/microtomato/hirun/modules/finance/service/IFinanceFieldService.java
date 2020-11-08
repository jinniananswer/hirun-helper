package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.NormalPayNoDTO;
import com.microtomato.hirun.modules.finance.entity.dto.ReceiveReceiptDTO;

/**
 * @program: hirun-helper
 * @description: 财务领域接口
 * @author: jinnian
 * @create: 2020-11-02 00:58
 **/
public interface IFinanceFieldService {
    IPage<FinanceOrderTaskDTO> queryBusinessReceipt(FinanceOrderTaskQueryDTO condition);

    IPage<NormalPayNoDTO> queryNonBusinessReceipt(NonCollectFeeQueryDTO queryCondition);

    void submitBusinessReceiveReceipt(ReceiveReceiptDTO data);

    void submitNonBusinessReceiveReceipt(ReceiveReceiptDTO data);
}
