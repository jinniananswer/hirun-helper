package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.sequence.impl.ComplainNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceComplain;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceComplainMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (ServiceComplain)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Service
@Slf4j
public class ServiceComplainServiceImpl extends ServiceImpl<ServiceComplainMapper, ServiceComplain> implements IServiceComplainService {

    @Autowired
    private ServiceComplainMapper serviceComplainMapper;

    @Autowired
    private IDualService dualService;

    @Autowired
    private IServiceGuaranteeService guaranteeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IEmployeeService employeeService;

    @Override
    public void saveComplainOrder(ComplainOrderInfoDTO infoDTO) {
        Long seq = dualService.nextval(ComplainNoCycleSeq.class);
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        if (StringUtils.isBlank(infoDTO.getComplainNo())) {
            String complainNo = "TS" + seq;
            if (ArrayUtils.isNotEmpty(infoDTO.getInsertRecords())) {
                for (ComplainOrderDTO orderDTO : infoDTO.getInsertRecords()) {
                    ServiceComplain serviceComplain = new ServiceComplain();
                    BeanUtils.copyProperties(orderDTO, serviceComplain);
                    serviceComplain.setCustomerId(infoDTO.getCustomerId());
                    serviceComplain.setStatus("1");
                    serviceComplain.setComplainNo(complainNo);
                    serviceComplain.setAcceptEmployeeId(employeeId);
                    this.baseMapper.insert(serviceComplain);
                }
            }
        } else {

            if (ArrayUtils.isNotEmpty(infoDTO.getInsertRecords())) {
                for (ComplainOrderDTO orderDTO : infoDTO.getInsertRecords()) {
                    ServiceComplain serviceComplain = new ServiceComplain();
                    BeanUtils.copyProperties(orderDTO, serviceComplain);
                    serviceComplain.setCustomerId(infoDTO.getCustomerId());
                    serviceComplain.setStatus("1");
                    serviceComplain.setComplainNo(infoDTO.getComplainNo());
                    serviceComplain.setAcceptEmployeeId(employeeId);
                    this.baseMapper.insert(serviceComplain);
                }
            }

            if (ArrayUtils.isNotEmpty(infoDTO.getRemoveRecords())) {
                for (ComplainOrderDTO removeRecord : infoDTO.getRemoveRecords()) {
                    ServiceComplain serviceComplain = new ServiceComplain();
                    BeanUtils.copyProperties(removeRecord, serviceComplain);
                    serviceComplain.setStatus("0");
                    this.baseMapper.updateById(serviceComplain);
                }
            }
            if (ArrayUtils.isNotEmpty(infoDTO.getUpdateRecords())) {
                for (ComplainOrderDTO updateRecord : infoDTO.getUpdateRecords()) {
                    ServiceComplain serviceComplain = new ServiceComplain();
                    BeanUtils.copyProperties(updateRecord, serviceComplain);
                    this.baseMapper.updateById(serviceComplain);
                }
            }
        }


    }

    @Override
    public List<ServicePendingOrderDTO> queryComplainPendingOrders(Long employeeId, String status) {
        return this.baseMapper.queryComplainPendingOrders(employeeId,status);
    }

    @Override
    public ComplainOrderRecordDTO queryComplainRecordInfo(Long orderId, Long customerId, String complainNo) {
        ComplainOrderRecordDTO complainOrderRecordDTO=new ComplainOrderRecordDTO();
        GuaranteeDTO guaranteeDTO=guaranteeService.queryCustomerGuaranteeInfo(orderId);
        //设置维修卡信息
        complainOrderRecordDTO.setGuaranteeInfo(guaranteeDTO);

        //查询历史投诉记录
        List<ServiceComplain> historyComplainRecord=this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getCustomerId,customerId)
                .in(ServiceComplain::getStatus, Arrays.asList(1,2,3)));

        if(ArrayUtils.isNotEmpty(historyComplainRecord)){
            List<ComplainOrderDTO> historyList=new ArrayList<>();
            for(ServiceComplain serviceComplain :historyComplainRecord){
                ComplainOrderDTO complainOrderDTO=new ComplainOrderDTO();
                BeanUtils.copyProperties(serviceComplain,complainOrderDTO);
                complainOrderDTO.setComplainTypeName(staticDataService.getCodeName("COMPLAIN_TYPE",serviceComplain.getComplainType()));
                complainOrderDTO.setComplainWayName(staticDataService.getCodeName("COMPLAIN_WAY",serviceComplain.getComplainWay()));
                complainOrderDTO.setAcceptEmployeeName(employeeService.getEmployeeNameEmployeeId(serviceComplain.getAcceptEmployeeId()));
                historyList.add(complainOrderDTO);
            }
            complainOrderRecordDTO.setHistoryComplainRecord(historyList);
        }

        //根据投诉编码查询投诉记录
        List<ServiceComplain> complainNoRecord=this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getComplainNo,complainNo)
                .in(ServiceComplain::getStatus, Arrays.asList(1,2)));

        if(ArrayUtils.isNotEmpty(complainNoRecord)){
            List<ComplainOrderDTO> complainNoList=new ArrayList<>();
            for(ServiceComplain serviceComplain:complainNoRecord){
                ComplainOrderDTO complainOrderDTO=new ComplainOrderDTO();
                BeanUtils.copyProperties(serviceComplain,complainOrderDTO);
                complainOrderDTO.setAcceptEmployeeName(employeeService.getEmployeeNameEmployeeId(serviceComplain.getAcceptEmployeeId()));
                complainNoList.add(complainOrderDTO);
            }
            complainOrderRecordDTO.setComplainRecord(complainNoList);
        }

        return complainOrderRecordDTO;
    }
}