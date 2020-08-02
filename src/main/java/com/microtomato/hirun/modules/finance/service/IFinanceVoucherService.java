package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.DesignRoyaltyDetailDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;

import java.util.List;

/**
 * 财务领款单表(FinanceVoucher)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
public interface IFinanceVoucherService extends IService<FinanceVoucher> {

    void voucherPreparationForSupply(List<FinanceVoucherDTO> financeVoucherDetails);

    void voucherPreparationForConstruction(List<FinanceVoucherDTO> financeVoucherDetails);

    void voucherPreparation(List<FinanceVoucherDTO> financeVoucherDetails);

}