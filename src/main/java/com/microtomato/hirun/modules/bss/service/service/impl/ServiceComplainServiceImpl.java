package com.microtomato.hirun.modules.bss.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.mybatis.sequence.impl.ComplainNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CustNoMaxCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceComplain;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import com.microtomato.hirun.modules.bss.service.mapper.ServiceComplainMapper;
import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.model.assignment.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IHousesService housesService;

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
        return this.baseMapper.queryComplainPendingOrders(employeeId, status);
    }

    @Override
    public ComplainOrderRecordDTO queryComplainRecordInfo(Long orderId, Long customerId, String complainNo) {
        ComplainOrderRecordDTO complainOrderRecordDTO = new ComplainOrderRecordDTO();
        GuaranteeDTO guaranteeDTO = guaranteeService.queryCustomerGuaranteeInfo(orderId);
        //设置维修卡信息
        complainOrderRecordDTO.setGuaranteeInfo(guaranteeDTO);

        //查询历史投诉记录
        List<ServiceComplain> historyComplainRecord = this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getCustomerId, customerId)
                .in(ServiceComplain::getStatus, Arrays.asList(1, 2, 3)));

        if (ArrayUtils.isNotEmpty(historyComplainRecord)) {
            List<ComplainOrderDTO> historyList = new ArrayList<>();
            for (ServiceComplain serviceComplain : historyComplainRecord) {
                ComplainOrderDTO complainOrderDTO = new ComplainOrderDTO();
                BeanUtils.copyProperties(serviceComplain, complainOrderDTO);
                complainOrderDTO.setComplainTypeName(staticDataService.getCodeName("COMPLAIN_TYPE", serviceComplain.getComplainType()));
                complainOrderDTO.setComplainWayName(staticDataService.getCodeName("COMPLAIN_WAY", serviceComplain.getComplainWay()));
                complainOrderDTO.setAcceptEmployeeName(employeeService.getEmployeeNameEmployeeId(serviceComplain.getAcceptEmployeeId()));
                historyList.add(complainOrderDTO);
            }
            complainOrderRecordDTO.setHistoryComplainRecord(historyList);
        }

        //根据投诉编码查询投诉记录
        List<ServiceComplain> complainNoRecord = this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getComplainNo, complainNo)
                .in(ServiceComplain::getStatus, Arrays.asList(1, 2)));

        if (ArrayUtils.isNotEmpty(complainNoRecord)) {
            List<ComplainOrderDTO> complainNoList = new ArrayList<>();
            for (ServiceComplain serviceComplain : complainNoRecord) {
                ComplainOrderDTO complainOrderDTO = new ComplainOrderDTO();
                BeanUtils.copyProperties(serviceComplain, complainOrderDTO);
                complainOrderDTO.setAcceptEmployeeName(employeeService.getEmployeeNameEmployeeId(serviceComplain.getAcceptEmployeeId()));
                complainNoList.add(complainOrderDTO);
            }
            complainOrderRecordDTO.setComplainRecord(complainNoList);
        }

        return complainOrderRecordDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void acceptComplain(ComplainOrderInfoDTO infoDTO) {
        String complainNo = infoDTO.getComplainNo();
        if (StringUtils.isBlank(complainNo)) {
            throw new NotFoundException("未找到对应的投诉编码,无法处理", ErrorKind.NOT_FOUND.getCode());
        }
        //保存一次前台数据
        this.saveComplainOrder(infoDTO);
        //根据投诉ID取所有新建状态的数据进行更新
        List<ServiceComplain> serviceComplains=this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getComplainNo,complainNo)
                .eq(ServiceComplain::getStatus,"1"));

        if(ArrayUtils.isNotEmpty(serviceComplains)){
            Long employeeId=WebContextUtils.getUserContext().getEmployeeId();
            for(ServiceComplain serviceComplain:serviceComplains){
                serviceComplain.setDealEmployeeId(employeeId);
                serviceComplain.setStatus("2");
                this.baseMapper.updateById(serviceComplain);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void finishComplainDeal(ComplainOrderInfoDTO infoDTO) {
        String complainNo = infoDTO.getComplainNo();
        if (StringUtils.isBlank(complainNo)) {
            throw new NotFoundException("未找到对应的投诉编码,无法处理", ErrorKind.NOT_FOUND.getCode());
        }
        //保存一次前台数据
        this.saveComplainOrder(infoDTO);
        //根据投诉ID取所有新建状态的数据进行更新
        List<ServiceComplain> serviceComplains=this.baseMapper.selectList(new QueryWrapper<ServiceComplain>().lambda()
                .eq(ServiceComplain::getComplainNo,complainNo)
                .in(ServiceComplain::getStatus,Arrays.asList(1, 2)));

        if(ArrayUtils.isNotEmpty(serviceComplains)){
            Long employeeId=WebContextUtils.getUserContext().getEmployeeId();
            for(ServiceComplain serviceComplain:serviceComplains){
                serviceComplain.setDealEmployeeId(employeeId);
                serviceComplain.setStatus("3");
                this.baseMapper.updateById(serviceComplain);
            }
        }
    }

    @Override
    public List<ComplainOrderDTO> queryComplainAllRecord(QueryComplainCondDTO condDTO) {

        QueryWrapper<QueryComplainCondDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(condDTO.getCustName()),"a.cust_name",condDTO.getCustName());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getComplainType()),"c.complain_type",condDTO.getComplainType());
        queryWrapper.eq(StringUtils.isNotBlank(condDTO.getComplainWay()),"c.complain_way",condDTO.getComplainWay());
        queryWrapper.eq(condDTO.getHouseId()!=null,"b.houses_id",condDTO.getHouseId());

        queryWrapper.apply("a.cust_id=b.cust_id and a.cust_id=c.customer_id");
        List<ComplainOrderDTO> list=this.baseMapper.queryComplainAllRecord(queryWrapper);
        if(ArrayUtils.isEmpty(list)){
            return null;
        }
        for(ComplainOrderDTO dto:list){
            dto.setComplainTypeName(staticDataService.getCodeName("COMPLAIN_TYPE", dto.getComplainType()));
            dto.setComplainWayName(staticDataService.getCodeName("COMPLAIN_WAY", dto.getComplainWay()));
            dto.setAcceptEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getAcceptEmployeeId()));
            dto.setHouseName(housesService.queryHouseName(dto.getHouseId()));
            dto.setStatusName(staticDataService.getCodeName("COMPLAIN_STATUS",dto.getStatus()));
        }
        return list;
    }
}