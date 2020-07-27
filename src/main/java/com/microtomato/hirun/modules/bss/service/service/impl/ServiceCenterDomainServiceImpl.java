package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.FinancePendingOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.FinancePendingTaskDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceRepairOrderMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceCenterDomainService;
import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * (ServiceRepairOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceCenterDomainServiceImpl extends ServiceImpl<ServiceRepairOrderMapper, ServiceRepairOrder> implements IServiceCenterDomainService {

    @Autowired
    private ServiceRepairOrderMapper serviceRepairOrderMapper;


    @Autowired
    private IServiceGuaranteeService guaranteeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IServiceComplainService complainService;

    @Override
    public List<ServicePendingTaskDTO> queryServicePendingTask() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<ServicePendingTaskDTO> result = new ArrayList<>();


        List<ServicePendingOrderDTO> repairOrders = this.serviceRepairOrderMapper.queryRepairPendingOrders(employeeId, "1,2");
        if(ArrayUtils.isNotEmpty(repairOrders)){
            Map<String, ServicePendingTaskDTO> temp = new HashMap<>();
            ServicePendingTaskDTO task=null;
            for(ServicePendingOrderDTO pendingOrderDTO:repairOrders){
                String status=pendingOrderDTO.getStatus();
                String statusName=staticDataService.getCodeName("REPAIR_STATUS",status);
                if(!temp.containsKey(status)){
                    task=new ServicePendingTaskDTO();
                    task.setStatus(status);
                    task.setStatusName(statusName);
                    temp.put(status,task);
                }else{
                    task=temp.get(status);
                }
                List<ServicePendingOrderDTO> orders=null;

                if(task.getOrders()==null){
                    orders=new ArrayList<>();
                    task.setOrders(orders);
                }else{
                    orders = task.getOrders();
                }
                orders.add(pendingOrderDTO);
            }
            Set<String> keys = temp.keySet();
            for (String key : keys) {
                result.add(temp.get(key));
            }
        }


        List<ServicePendingOrderDTO> complainOrders = this.complainService.queryComplainPendingOrders(employeeId, "1,2");

        if(ArrayUtils.isNotEmpty(complainOrders)){
            Map<String, ServicePendingTaskDTO> temp = new HashMap<>();
            ServicePendingTaskDTO task=null;
            for(ServicePendingOrderDTO pendingOrderDTO:complainOrders){
                String status=pendingOrderDTO.getStatus();
                String statusName=staticDataService.getCodeName("COMPLAIN_STATUS",status);
                if(!temp.containsKey(status)){
                    task=new ServicePendingTaskDTO();
                    task.setStatus(status);
                    task.setStatusName(statusName);
                    temp.put(status,task);
                }else{
                    task=temp.get(status);
                }
                List<ServicePendingOrderDTO> orders=null;

                if(task.getOrders()==null){
                    orders=new ArrayList<>();
                    task.setOrders(orders);
                }else{
                    orders = task.getOrders();
                }
                orders.add(pendingOrderDTO);
            }
            Set<String> keys = temp.keySet();
            for (String key : keys) {
                result.add(temp.get(key));
            }
        }

        return result;
    }
}