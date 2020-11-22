package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.dto.QueryVoucherAuditDTO;
import com.microtomato.hirun.modules.finance.entity.dto.VoucherDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    void voucherPreparationForOther(List<FinanceVoucherDTO> financeVoucherDetails);

    void voucherPreparation(List<FinanceVoucherDTO> financeVoucherDetails);

    List<DecoratorInfoDTO> selectDecorator(DecoratorInfoDTO decoratorInfoDTO);

    IPage<FinanceVoucherDTO> queryVoucherSupplyInfo(QueryVoucherAuditDTO condition);

    void auditForSupplyPass(List<FinanceVoucherDTO> financeVoucherDTO);

    void auditForSupplyReject(List<FinanceVoucherDTO> financeVoucherDTO);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void createVoucher(VoucherDTO request);
}