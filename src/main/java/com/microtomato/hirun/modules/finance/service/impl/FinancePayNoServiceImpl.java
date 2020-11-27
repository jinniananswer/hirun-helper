package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.sequence.impl.BillNoCycle;
import com.microtomato.hirun.framework.mybatis.sequence.impl.FinancePayNoCycle;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.PaymentDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinancePayNoDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayMoney;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayNo;
import com.microtomato.hirun.modules.finance.exception.FinanceException;
import com.microtomato.hirun.modules.finance.mapper.FinancePayNoMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceAcctService;
import com.microtomato.hirun.modules.finance.service.IFinancePayMoneyService;
import com.microtomato.hirun.modules.finance.service.IFinancePayNoService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 会计付款流水表(FinancePayNo)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class FinancePayNoServiceImpl extends ServiceImpl<FinancePayNoMapper, FinancePayNo> implements IFinancePayNoService {

    @Autowired
    private FinancePayNoMapper financePayNoMapper;

    @Autowired
    private IDualService dualService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IFinancePayMoneyService financePayMoneyService;

    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @Autowired
    private IFinanceAcctService financeAcctService;

    /**
     * 根据付款流水找到付款数据
     * @param payNo
     * @return
     */
    @Override
    public FinancePayNo getByPayNo(Long payNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(Wrappers.<FinancePayNo>lambdaQuery()
                .eq(FinancePayNo::getPayNo, payNo)
                .ge(FinancePayNo::getEndDate, now)
                .le(FinancePayNo::getStartDate, now), false);
    }

    @Override
    public FinancePayNo getByVoucherNo(String voucherNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(Wrappers.<FinancePayNo>lambdaQuery()
                .eq(FinancePayNo::getVoucherNo, voucherNo)
                .ge(FinancePayNo::getEndDate, now)
                .le(FinancePayNo::getStartDate, now), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createPayNo(FinancePayNoDTO pay) {
        if (StringUtils.isBlank(pay.getVoucherNo())) {
            throw new NotFoundException("领款单号缺失", ErrorKind.NOT_FOUND.getCode());
        }

        Double totalMoney = pay.getTotalMoney();
        if (totalMoney == null) {
            throw new NotFoundException("付款总金额缺失", ErrorKind.NOT_FOUND.getCode());
        }

        FinancePayNo financePayNo = new FinancePayNo();
        BeanUtils.copyProperties(pay, financePayNo);

        if (pay.getPayNo() == null) {
            financePayNo.setPayNo(dualService.nextval(FinancePayNoCycle.class));
        }

        if (StringUtils.isBlank(pay.getBillNo())) {
            financePayNo.setBillNo("FL" + dualService.nextval(BillNoCycle.class));
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        financePayNo.setPayEmployeeId(employeeId);

        EmployeeJobRole employeeJobRole = this.employeeJobRoleService.queryLast(employeeId);
        if (employeeJobRole != null) {
            financePayNo.setOrgId(employeeJobRole.getOrgId());
        }

        Long money = new Long(Math.round(totalMoney * 100));
        financePayNo.setTotalMoney(money * -1);

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        financePayNo.setStartDate(now);
        financePayNo.setEndDate(TimeUtils.getForeverTime());

        List<FinancePayMoney> payMonies = new ArrayList<>();

        LocalDateTime forever = TimeUtils.getForeverTime();
        List<PaymentDTO> payments = pay.getPayments();
        Long payTotal = 0L;
        if (ArrayUtils.isNotEmpty(payments)) {
            for (PaymentDTO payment : payments) {
                FinancePayMoney payMoney = new FinancePayMoney();
                payMoney.setPaymentType(payment.getPaymentType());
                payMoney.setPaymentId(payment.getPaymentId());
                Double moneyDouble = payment.getMoney();
                if (moneyDouble == null || Math.abs(moneyDouble) <= 0.001) {
                    continue;
                }
                Long fee = new Long(Math.round(moneyDouble * 100));
                payMoney.setMoney(fee);
                payMoney.setPayNo(financePayNo.getPayNo());
                payMoney.setStartDate(now);
                payMoney.setEndDate(forever);
                payMonies.add(payMoney);
                payTotal += fee;
            }
        }

        if (!payTotal.equals(financePayNo.getTotalMoney())) {
            throw new FinanceException(FinanceException.FinanceExceptionEnum.PAY_MUST_EQUAL_PAYITEM);
        }

        this.save(financePayNo);
        if (ArrayUtils.isNotEmpty(payMonies)) {
            this.financePayMoneyService.saveBatch(payMonies);
        }

        this.financeVoucherService.updatePay(pay.getVoucherNo(), "5", employeeId, financePayNo.getPayDate());
    }

    @Override
    public FinancePayNoDTO getFinancePay(String voucherNo) {
        FinancePayNo financePayNo = this.getByVoucherNo(voucherNo);
        if (financePayNo == null) {
            return null;
        }
        FinancePayNoDTO pay = new FinancePayNoDTO();
        BeanUtils.copyProperties(financePayNo, pay);
        Long totalMoney = financePayNo.getTotalMoney();
        if (totalMoney != null) {
            pay.setTotalMoney(totalMoney / 100d);
        }

        Long payNo = financePayNo.getPayNo();
        List<FinancePayMoney> payMonies = this.financePayMoneyService.queryByPayNo(payNo);
        if (ArrayUtils.isEmpty(payMonies)) {
            return pay;
        }

        List<PaymentDTO> payments = this.financeAcctService.queryPayments();
        if (ArrayUtils.isEmpty(payments)) {
            return pay;
        }

        List<PaymentDTO> showPayments = new ArrayList<>();
        for (PaymentDTO payment : payments) {
            Long paymentId = payment.getPaymentId();
            for (FinancePayMoney payMoney : payMonies) {
                if (payMoney.getPaymentId().equals(paymentId)) {
                    Long money = payMoney.getMoney();
                    if (money != null) {
                        payment.setMoney(money / 100d);
                        showPayments.add(payment);
                    }
                }
            }
        }

        if (ArrayUtils.isNotEmpty(showPayments)) {
            pay.setPayments(showPayments);
        }
        return pay;
    }
}