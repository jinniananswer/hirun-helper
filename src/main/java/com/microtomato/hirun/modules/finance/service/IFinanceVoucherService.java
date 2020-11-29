package com.microtomato.hirun.modules.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.finance.entity.dto.*;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    IPage<VoucherResultDTO> queryReviewVouchers(QueryVoucherDTO request);

    FinanceVoucher getByVoucherNo(String voucherNo);

    void review(List<VoucherResultDTO> datas, boolean pass);

    void audit(List<VoucherResultDTO> datas, boolean pass);

    void auditSingleNo(Long id, String auditComment);

    void handVoucher(Long voucherId);

    void receiveVoucher(Long voucherId, boolean pass);

    void deleteVouchers(List<VoucherResultDTO> datas);

    VoucherResultDTO getVoucher(Long id);

    void updatePay(String voucherNo, String auditStatus, Long cashierEmployeeId, LocalDate payDate);
}