package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.service.entity.dto.GuaranteeDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceRepairOrderMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void saveRepairRecord(RepairOrderInfoDTO infoDTO) {
        Long seq = dualService.nextval(CustNoMaxCycleSeq.class);
        String repairNo="";
        if(ArrayUtils.isEmpty(infoDTO.getRemoveRecords())&&ArrayUtils.isEmpty(infoDTO.getUpdateRecords())&&ArrayUtils.isNotEmpty(infoDTO.getInsertRecords())){
            repairNo="WX"+seq;
        }else{
            if(ArrayUtils.isNotEmpty(infoDTO.getUpdateRecords())){
                repairNo=infoDTO.getUpdateRecords().get(0).getRepairNo();
            }else if(ArrayUtils.isNotEmpty(infoDTO.getRemoveRecords())){
                repairNo=infoDTO.getRemoveRecords().get(0).getRepairNo();
            }
        }


        List<ServiceRepairOrder> addList=new ArrayList<ServiceRepairOrder>();
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
}