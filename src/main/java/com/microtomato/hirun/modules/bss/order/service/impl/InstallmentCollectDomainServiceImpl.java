package com.microtomato.hirun.modules.bss.order.service.impl;


import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerSalaryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 分期收取工程款领域服务实现类
 * @author: liuhui
 * @create: 2020-03-05 21:11
 **/
@Service
@Slf4j
public class InstallmentCollectDomainServiceImpl implements IInstallmentCollectDomainService {
    @Autowired
    private IOrderWorkerSalaryService salaryService;

    @Autowired
    private IFeeDomainService feeDomainService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderFeeItemService feeItemService;

    @Autowired
    private IOrderWorkerService workerService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveLastCollectionFee(LastInstallmentCollectionDTO dto) {
        //保存工人工资
        OrderWorkerSalaryDTO workerSalary = dto.getWorkerSalaryDTO();
        salaryService.updateWorkerSalary(3, workerSalary);
        //保存费用
        LastInstallmentInfoDTO lastInstallmentInfoDTO = dto.getLastInstallmentInfoDTO();
        List<FeeDTO> feeDTOList = new ArrayList<>();
        //木制品费
        this.buildFeeList(6L, lastInstallmentInfoDTO.getWoodProductFee(), feeDTOList);
        //门费用
        this.buildFeeList(7L, lastInstallmentInfoDTO.getDoorFee(), feeDTOList);
        //家具费用
        this.buildFeeList(8L, lastInstallmentInfoDTO.getFurnitureFee(), feeDTOList);
        //税金
        this.buildFeeList(15L, lastInstallmentInfoDTO.getTaxFee(), feeDTOList);
        //基础装修款
        this.buildFeeList(5L, lastInstallmentInfoDTO.getBaseDecorationFee(), feeDTOList);
        //其他款项
        this.buildFeeList(9L, lastInstallmentInfoDTO.getOtherFee(), feeDTOList);

        feeDomainService.createOrderFee(lastInstallmentInfoDTO.getOrderId(), "2", 3, feeDTOList);
        //更新财务人员
        if(lastInstallmentInfoDTO.getFinanceEmployeeId()!=null){
            workerService.updateOrderWorker(lastInstallmentInfoDTO.getOrderId(),35L,lastInstallmentInfoDTO.getFinanceEmployeeId());
        }
    }

    @Override
    public LastInstallmentCollectionDTO queryLastInstallmentCollect(Long orderId) {
        LastInstallmentCollectionDTO lastInstallmentCollectionDTO = new LastInstallmentCollectionDTO();

        List<OrderWorkerSalaryDTO> workerSalaryDTO = salaryService.queryOrderWorkerSalary(3, orderId);
        if (!ArrayUtils.isEmpty(workerSalaryDTO)) {
            lastInstallmentCollectionDTO.setWorkerSalaryDTO(workerSalaryDTO.get(0));
        }
        LastInstallmentInfoDTO lastInstallmentInfoDTO = new LastInstallmentInfoDTO();
        //已付尾款
        Long chargedLastFee = feeDomainService.getPayedMoney(orderId, "2", 3);
        lastInstallmentInfoDTO.setChargedLastFee((chargedLastFee.doubleValue() / 100));
        //所有工程款尾款
        Long chargedAllFee = feeDomainService.getPayedMoney(orderId, "2", null);
        lastInstallmentInfoDTO.setChargedAllFee((chargedAllFee.doubleValue() / 100));
        //已付橱柜
        Long chargedCupboardFee = feeDomainService.getPayedMoney(orderId, "3", null);
        lastInstallmentInfoDTO.setChargedCupboardFee((chargedCupboardFee.doubleValue() / 100));
        //已付主材
        Long chargedMaterFee = feeDomainService.getPayedMoney(orderId, "4", null);
        lastInstallmentInfoDTO.setChargedMaterialFee((chargedMaterFee.doubleValue() / 100));

        this.queryOrderPayItemToDTO(orderId,lastInstallmentInfoDTO);

        lastInstallmentCollectionDTO.setLastInstallmentInfoDTO(lastInstallmentInfoDTO);

        return lastInstallmentCollectionDTO;
    }

    @Override
    public void applyFinanceAuditLast(LastInstallmentCollectionDTO dto) {
        this.saveLastCollectionFee(dto);
        orderDomainService.orderStatusTrans(dto.getLastInstallmentInfoDTO().getOrderId(), "NEXT");
    }

    private void buildFeeList(Long feeItemId, Double fee, List<FeeDTO> list) {
        FeeDTO feeDTO = new FeeDTO();
        feeDTO.setMoney(fee);
        feeDTO.setFeeItemId(feeItemId);
        list.add(feeDTO);
    }

    /**
     * 将竖表数据变成横表数据
     * @param orderId
     * @param lastInstallmentInfoDTO
     * @return
     */
    private LastInstallmentInfoDTO queryOrderPayItemToDTO(Long orderId, LastInstallmentInfoDTO lastInstallmentInfoDTO) {
        List<Long> feeItemList = new ArrayList<Long>();
        feeItemList.add(5L);
        feeItemList.add(6L);
        feeItemList.add(7L);
        feeItemList.add(8L);
        feeItemList.add(15L);
        List<OrderFeeItem> list = feeItemService.queryByOrderIdTypePeriod(orderId, "2", 3);

        if (ArrayUtils.isEmpty(list)) {
            return lastInstallmentInfoDTO;
        }
        for (OrderFeeItem orderFeeItem : list) {
            if (orderFeeItem.getFeeItemId().equals(5L)) {
                lastInstallmentInfoDTO.setBaseDecorationFee(orderFeeItem.getFee().doubleValue() / 100);
            } else if (orderFeeItem.getFeeItemId().equals(6L)) {
                lastInstallmentInfoDTO.setWoodProductFee(orderFeeItem.getFee().doubleValue() / 100);
            } else if (orderFeeItem.getFeeItemId().equals(7L)) {
                lastInstallmentInfoDTO.setDoorFee(orderFeeItem.getFee().doubleValue() / 100);
            } else if (orderFeeItem.getFeeItemId().equals(8L)) {
                lastInstallmentInfoDTO.setFurnitureFee(orderFeeItem.getFee().doubleValue() / 100);
            } else if (orderFeeItem.getFeeItemId().equals(9L)) {
                lastInstallmentInfoDTO.setOtherFee(orderFeeItem.getFee().doubleValue() / 100);
            } else if (orderFeeItem.getFeeItemId().equals(15L)) {
                lastInstallmentInfoDTO.setTaxFee(orderFeeItem.getFee().doubleValue() / 100);
            }

        }
        return lastInstallmentInfoDTO;
    }
}
