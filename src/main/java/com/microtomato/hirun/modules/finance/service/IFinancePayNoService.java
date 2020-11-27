package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.finance.entity.dto.FinancePayNoDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayNo;

/**
 * 会计付款流水表(FinancePayNo)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
public interface IFinancePayNoService extends IService<FinancePayNo> {

    FinancePayNo getByPayNo(Long payNo);

    FinancePayNo getByVoucherNo(String voucherNo);

    void createPayNo(FinancePayNoDTO pay);

    FinancePayNoDTO getFinancePay(String voucherNo);
}