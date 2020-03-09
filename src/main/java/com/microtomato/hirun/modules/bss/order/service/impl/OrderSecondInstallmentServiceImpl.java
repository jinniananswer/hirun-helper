package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.config.entity.consts.FeeConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderSecondInstallmentServiceImpl implements IOrderSecondInstallmentService {

    @Autowired
    private IOrderWorkerService workerService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IFeeDomainService feeDomainService;

    @Autowired
    private IOrderWorkerSalaryService orderWorkerSalaryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void secondInstallmentCollect(SecondInstallmentCollectionDTO dto) {
        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);

        //保存费用信息到各个表
        List<FeeDTO> fees = new ArrayList<>();
        //基础装修费
        FeeDTO baseFee = new FeeDTO();
        baseFee.setFeeItemId(5L);
        baseFee.setMoney(dto.getBaseDecorationFee().doubleValue()/100);
        fees.add(baseFee);
        //门总金额
        FeeDTO doorFee = new FeeDTO();
        doorFee.setFeeItemId(7L);
        doorFee.setMoney(dto.getDoorFee().doubleValue()/100);
        fees.add(doorFee);
        //家具总金额
        FeeDTO furnitureFee = new FeeDTO();
        furnitureFee.setFeeItemId(8L);
        furnitureFee.setMoney(dto.getFurnitureFee().doubleValue()/100);
        fees.add(furnitureFee);
        //其他金额
        FeeDTO otherFee = new FeeDTO();
        otherFee.setFeeItemId(9L);
        otherFee.setMoney(dto.getOtherFee().doubleValue()/100);
        fees.add(otherFee);
        //税金额
        FeeDTO taxFee = new FeeDTO();
        taxFee.setFeeItemId(15L);
        taxFee.setMoney(dto.getTaxFee().doubleValue()/100);
        fees.add(taxFee);
        feeDomainService.createOrderFee(dto.getOrderId(), FeeConst.FEE_TYPE_PROJECT, FeeConst.FEE_PERIOD_SECOND, fees);

        //更新员工工资
//        orderWorkerSalaryService.updateWorkerSalary();

        workerService.updateOrderWorker(dto.getOrderId(), 35L, dto.getFinanceEmployeeId());
    }
}
