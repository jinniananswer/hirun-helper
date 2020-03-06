package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.mybatis.sequence.impl.FeeNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.consts.FeeConst;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.FeePayRelCfg;
import com.microtomato.hirun.modules.bss.config.service.IFeeItemCfgService;
import com.microtomato.hirun.modules.bss.config.service.IFeePayRelCfgService;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerSalaryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveLastCollectionFee(LastInstallmentCollectionDTO dto) {
        //保存工人工资
        OrderWorkerSalaryDTO workerSalary=dto.getWorkerSalaryDTO();
        salaryService.updateWorkerSalary(3,workerSalary);
        //保存费用
        LastInstallmentInfoDTO lastInstallmentInfoDTO=dto.getLastInstallmentInfoDTO();
        List<FeeDTO> feeDTOList=new ArrayList<>();
        //木制品费
        this.buildFeeList(6L,lastInstallmentInfoDTO.getWoodProductFee(),feeDTOList);
        //门费用
        this.buildFeeList(7L,lastInstallmentInfoDTO.getDoorFee(),feeDTOList);
        //家具费用
        this.buildFeeList(8L,lastInstallmentInfoDTO.getFurnitureFee(),feeDTOList);
        //税金
        this.buildFeeList(15L,lastInstallmentInfoDTO.getTaxFee(),feeDTOList);
        //基础装修款
        this.buildFeeList(5L,lastInstallmentInfoDTO.getBaseDecorationFee(),feeDTOList);
        //todo 其他款项


        feeDomainService.createOrderFee(lastInstallmentInfoDTO.getOrderId(),"2",3,feeDTOList);
    }

    @Override
    public LastInstallmentCollectionDTO queryLastInstallmentCollect(Long orderId) {
        LastInstallmentCollectionDTO lastInstallmentCollectionDTO=new LastInstallmentCollectionDTO();

        List<OrderWorkerSalaryDTO> workerSalaryDTO=salaryService.queryOrderWorkerSalary(3,orderId);
        if(!ArrayUtils.isEmpty(workerSalaryDTO)){
            lastInstallmentCollectionDTO.setWorkerSalaryDTO(workerSalaryDTO.get(0));
        }
        Long chargedAllFee=feeDomainService.getPayedMoney(orderId,"2",3);
        LastInstallmentInfoDTO lastInstallmentInfoDTO=new LastInstallmentInfoDTO();
        lastInstallmentInfoDTO.setChargedAllFee((chargedAllFee.doubleValue()/100));
        lastInstallmentCollectionDTO.setLastInstallmentInfoDTO(lastInstallmentInfoDTO);

        return lastInstallmentCollectionDTO;
    }

    @Override
    public void applyFinanceAuditLast(LastInstallmentCollectionDTO dto) {
        this.saveLastCollectionFee(dto);
        orderDomainService.orderStatusTrans(dto.getLastInstallmentInfoDTO().getOrderId(),"NEXT");
    }

    private void buildFeeList(Long feeItemId,Double fee,List<FeeDTO> list){
        FeeDTO feeDTO=new FeeDTO();
        feeDTO.setMoney(fee);
        feeDTO.setFeeItemId(feeItemId);
        list.add(feeDTO);
    }
}
