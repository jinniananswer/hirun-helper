package com.microtomato.hirun.modules.bss.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private IOrderContractService orderContractService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveLastCollectionFee(LastInstallmentCollectionDTO dto) {
        //保存工人工资
        OrderWorkerSalaryDTO workerSalary = dto.getWorkerSalaryDTO();
        salaryService.updateWorkerSalary(3, workerSalary);
        //保存费用
        LastInstallmentInfoDTO lastInstallmentInfoDTO = dto.getLastInstallmentInfoDTO();
        List<FeeDTO> feeDTOList = new ArrayList<>();

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
        if (lastInstallmentInfoDTO.getFinanceEmployeeId() != null) {
            workerService.updateOrderWorker(lastInstallmentInfoDTO.getOrderId(), 35L, lastInstallmentInfoDTO.getFinanceEmployeeId());
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
        Long chargedCupboardFee = feeDomainService.getPayedMoney(orderId, "4", null);
        lastInstallmentInfoDTO.setChargedCupboardFee((chargedCupboardFee.doubleValue() / 100));
        //已付主材
        Long chargedMaterFee = feeDomainService.getPayedMoney(orderId, "3", null);
        lastInstallmentInfoDTO.setChargedMaterialFee((chargedMaterFee.doubleValue() / 100));
        //尾款项
        this.queryOrderPayItemToDTO(orderId, lastInstallmentInfoDTO);
        //财务人员
        List<OrderWorkerDTO> orderWorkersDTO = workerService.queryByOrderId(orderId);
        if(orderWorkersDTO != null) {
            for(OrderWorkerDTO orderWorkerDTO : orderWorkersDTO) {
                if(orderWorkerDTO.getRoleId() == 35L) {
                    lastInstallmentInfoDTO.setFinanceEmployeeId(orderWorkerDTO.getEmployeeId());
                    lastInstallmentInfoDTO.setFinanceEmployeeName(orderWorkerDTO.getName());
                }
            }
        }

        lastInstallmentCollectionDTO.setLastInstallmentInfoDTO(lastInstallmentInfoDTO);



        return lastInstallmentCollectionDTO;
    }

    @Override
    public void applyFinanceAuditLast(LastInstallmentCollectionDTO dto) {
        this.saveLastCollectionFee(dto);
        orderDomainService.orderStatusTrans(dto.getLastInstallmentInfoDTO().getOrderId(), "NEXT");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitWoodContract(WoodContractDTO dto) {
        UserContext userContext = WebContextUtils.getUserContext();
        //保存合同信息
        OrderContract orderContract = new OrderContract();
        BeanUtils.copyProperties(dto, orderContract);
        orderContract.setContractType("1");
        orderContract.setOrgId(userContext.getOrgId());
        orderContractService.save(orderContract);
        //保存费用信息
        List<FeeDTO> feeDTOList = new ArrayList<>();
        this.buildFeeList(6L, dto.getContractFee(), feeDTOList);
        this.buildFeeList(7L, dto.getDoorFee(), feeDTOList);
        this.buildFeeList(8L, dto.getFurnitureFee(), feeDTOList);
        this.buildFeeList(15L, dto.getTaxFee(), feeDTOList);
        feeDomainService.createOrderFee(dto.getOrderId(), "2", 1, feeDTOList);
        //保存orderWorker
        if (dto.getProjectEmployeeId() != null) {
            workerService.updateOrderWorker(dto.getOrderId(), 33L, dto.getProjectEmployeeId());
        }
        if (dto.getFinanceEmployeeId() != null) {
            workerService.updateOrderWorker(dto.getOrderId(), 34L, dto.getFinanceEmployeeId());
        }
        //状态转换
        orderDomainService.orderStatusTrans(dto.getOrderId(), "NEXT");
    }

    @Override
    public void auditWoodFirstCollect(WoodContractDTO dto) {
        orderDomainService.orderStatusTrans(dto.getOrderId(), "NEXT");
    }

    @Override
    public void submitWoodLastCollect(WoodContractDTO dto) {
        orderDomainService.orderStatusTrans(dto.getOrderId(), "NEXT");
    }

    @Override
    public void auditWoodLastCollect(WoodContractDTO dto) {
        orderDomainService.orderStatusTrans(dto.getOrderId(), "NEXT");
    }

    private void buildFeeList(Long feeItemId, Double fee, List<FeeDTO> list) {
        FeeDTO feeDTO = new FeeDTO();
        feeDTO.setMoney(fee);
        feeDTO.setFeeItemId(feeItemId);
        list.add(feeDTO);
    }

    /**
     * 将竖表数据变成横表数据
     *
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

    public WoodContractDTO queryWoodContract(Long orderId) {
        WoodContractDTO dto = new WoodContractDTO();
        //拼装合同信息
        OrderContract orderContract = orderContractService.getOne(new QueryWrapper<OrderContract>().lambda().eq(OrderContract::getOrderId, orderId)
                .eq(OrderContract::getContractType, "1"));
        BeanUtils.copyProperties(orderContract, dto);
        //拼装已收木制品信息
        Long chargedWoodFee = feeDomainService.getPayedMoney(orderId, "3", null);
        dto.setChargedWoodFee(chargedWoodFee.doubleValue() / 100);
        //拼装合同费用信息 todo
        feeItemService.queryByOrderIdTypePeriod(orderId, "2", 1);
        //拼装参与人业务的人员信息
        List<OrderWorkerDTO> workerList = workerService.queryByOrderId(orderId);
        if (ArrayUtils.isEmpty(workerList)) {
            return dto;
        }

        for (OrderWorkerDTO workerDTO : workerList) {
            if (workerDTO.getRoleId().equals(33L)) {
                dto.setProjectEmployeeId(workerDTO.getEmployeeId());
            } else if (workerDTO.getRoleId().equals(34L)) {
                dto.setFinanceEmployeeId(workerDTO.getEmployeeId());
            }
        }
        return dto;
    }
}
