package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceRepairOrderMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (ServiceRepairOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceRepairOrderServiceImpl extends ServiceImpl<ServiceRepairOrderMapper, ServiceRepairOrder> implements IServiceRepairOrderService {

    @Autowired
    private ServiceRepairOrderMapper serviceRepairOrderMapper;

    @Autowired
    private IDualService dualService;

    @Autowired
    private IServiceGuaranteeService guaranteeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRepairRecord(RepairOrderInfoDTO infoDTO) {
        Long seq = dualService.nextval(CustNoMaxCycleSeq.class);

        if(StringUtils.isBlank(infoDTO.getRepairNo())){
            String repairNo="";
            repairNo="WX"+seq;
            if(ArrayUtils.isNotEmpty(infoDTO.getInsertRecords())){
                for(RepairOrderDTO orderDTO:infoDTO.getInsertRecords()){
                    ServiceRepairOrder serviceRepairOrder=new ServiceRepairOrder();
                    BeanUtils.copyProperties(orderDTO,serviceRepairOrder);
                    serviceRepairOrder.setCustomerId(infoDTO.getCustomerId());
                    serviceRepairOrder.setStatus("1");
                    serviceRepairOrder.setRepairNo(repairNo);
                    this.baseMapper.insert(serviceRepairOrder);
                }
            }
        }else{
            if(ArrayUtils.isNotEmpty(infoDTO.getInsertRecords())){
                for(RepairOrderDTO orderDTO:infoDTO.getInsertRecords()){
                    ServiceRepairOrder serviceRepairOrder=new ServiceRepairOrder();
                    BeanUtils.copyProperties(orderDTO,serviceRepairOrder);
                    serviceRepairOrder.setCustomerId(infoDTO.getCustomerId());
                    serviceRepairOrder.setStatus("1");
                    serviceRepairOrder.setRepairNo(infoDTO.getRepairNo());
                    this.baseMapper.insert(serviceRepairOrder);
                }
            }

            if (ArrayUtils.isNotEmpty(infoDTO.getRemoveRecords())) {
                for (RepairOrderDTO removeRecord : infoDTO.getRemoveRecords()) {
                    ServiceRepairOrder repairOrder = new ServiceRepairOrder();
                    BeanUtils.copyProperties(removeRecord, repairOrder);
                    repairOrder.setStatus("0");
                    this.baseMapper.updateById(repairOrder);
                }
            }

            if (ArrayUtils.isNotEmpty(infoDTO.getUpdateRecords())) {
                for (RepairOrderDTO updateRecord : infoDTO.getUpdateRecords()) {
                    ServiceRepairOrder repairOrder = new ServiceRepairOrder();
                    BeanUtils.copyProperties(updateRecord, repairOrder);
                    this.baseMapper.updateById(repairOrder);
                }
            }
        }


    }

    @Override
    public RepairOrderRecordDTO queryRepairRecordInfo(Long orderId, Long customerId, String repairNo) {
        RepairOrderRecordDTO repairOrderRecordDTO=new RepairOrderRecordDTO();
        GuaranteeDTO guaranteeDTO=guaranteeService.queryCustomerGuaranteeInfo(orderId);
        //设置维修卡信息
        repairOrderRecordDTO.setGuaranteeInfo(guaranteeDTO);
        //查询历史维修记录
        List<ServiceRepairOrder> historyRepairRecord=this.baseMapper.selectList(new QueryWrapper<ServiceRepairOrder>().lambda()
                .eq(ServiceRepairOrder::getCustomerId,customerId)
                .in(ServiceRepairOrder::getStatus, Arrays.asList(1,2)));

        if(ArrayUtils.isNotEmpty(historyRepairRecord)){
            List<RepairOrderDTO> historyList=new ArrayList<>();
            for(ServiceRepairOrder serviceRepairOrder:historyRepairRecord){
                RepairOrderDTO repairOrderDTO=new RepairOrderDTO();
                BeanUtils.copyProperties(serviceRepairOrder,repairOrderDTO);
                repairOrderDTO.setIsFeeName(staticDataService.getCodeName("YES_NO",serviceRepairOrder.getIsFee()));
                repairOrderDTO.setRepairWorkerTypeName(staticDataService.getCodeName("REPAIR_WORKER_TYPE",serviceRepairOrder.getRepairWorkerType()));
                historyList.add(repairOrderDTO);
            }
            repairOrderRecordDTO.setHistoryRepairRecord(historyList);
        }

        //根据维修卡号查询维修记录
        List<ServiceRepairOrder> repairNoRecord=this.baseMapper.selectList(new QueryWrapper<ServiceRepairOrder>().lambda()
                .eq(ServiceRepairOrder::getRepairNo,repairNo)
                .in(ServiceRepairOrder::getStatus, Arrays.asList(1,2)));

        if(ArrayUtils.isNotEmpty(repairNoRecord)){
            List<RepairOrderDTO> repairNoList=new ArrayList<>();
            for(ServiceRepairOrder serviceRepairOrder:repairNoRecord){
                RepairOrderDTO repairOrderDTO=new RepairOrderDTO();
                BeanUtils.copyProperties(serviceRepairOrder,repairOrderDTO);
                repairNoList.add(repairOrderDTO);
            }
            repairOrderRecordDTO.setRepairNoRecord(repairNoList);
        }

        return repairOrderRecordDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void nextStep(RepairOrderInfoDTO infoDTO) {
        //保存数据
        this.saveRepairRecord(infoDTO);
        //取数据做状态更新
        List<ServiceRepairOrder> repairNoRecord=this.baseMapper.selectList(new QueryWrapper<ServiceRepairOrder>().lambda()
                .eq(ServiceRepairOrder::getRepairNo,infoDTO.getRepairNo())
                .in(ServiceRepairOrder::getStatus, Arrays.asList(1,2)));

        if(ArrayUtils.isNotEmpty(repairNoRecord)){
            for(ServiceRepairOrder serviceRepairOrder:repairNoRecord){
                if(StringUtils.equals(serviceRepairOrder.getStatus(),"1")){
                    serviceRepairOrder.setStatus("2");
                }else if(StringUtils.equals(serviceRepairOrder.getStatus(),"2")){
                    serviceRepairOrder.setStatus("3");
                }
                this.baseMapper.updateById(serviceRepairOrder);
            }
        }
    }
}