package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liuhui
 * @program: hirun-helper
 * @description: 员工合同领域服务实现类
 **/
@Slf4j
@Service
public class EmployeeContractDomainServiceImpl implements IEmployeeContractDomainService {

    @Autowired
    private IEmployeeContractService employeeContractService;


    @Override
    @DS("ins")
    public void createEmployeeContract(EmployeeContract employeeContract) {
        String contractType = employeeContract.getContractType();
        //时间变更协议只允许新增一次
        if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_POSTPONE, contractType)) {
            List<EmployeeContract> contractList = employeeContractService.queryVaildContractByEmpIdAndType(employeeContract.getEmployeeId(), contractType);
            if (contractList.size() > 0) {
                throw new BaseException("时间变更只允许新增一次!", 400001);
            }
        }
        //如果合同类型为保密协议或者培训协议，在后台提交时设置开始时间和结束时间
        if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_TRAIN, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_SECRET, contractType)) {
            employeeContract.setContractStartTime(employeeContract.getContractSignTime());
            employeeContract.setContractEndTime(TimeUtils.stringToLocalDateTime("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        } else if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_ROLE, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_PLACE, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_PROBLATION, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_OTHER, contractType)) {
            //如果变更类型为合同变更设置变更协议的结束时间为当前合同的结束时间，如果未找到则设置时间为2099年
            employeeContract.setContractStartTime(employeeContract.getContractSignTime());

            EmployeeContract validContract = employeeContractService.getById(employeeContract.getParentContractId());
            if (validContract == null) {
                employeeContract.setContractEndTime(TimeUtils.stringToLocalDateTime("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
            } else {
                employeeContract.setContractEndTime(validContract.getContractEndTime());
            }

        }
        employeeContract.setStatus(EmployeeConst.CONTRACT_STATUS_NORMAL);
        employeeContractService.save(employeeContract);
    }

    @Override
    public IPage<EmployeeContract> queryEmployeeContracts(Long employeeId, Page<EmployeeContract> page) {
        IPage<EmployeeContract> employeeContractIPage = employeeContractService.queryEmployeeContracts(employeeId, page);
        if (employeeContractIPage.getRecords().size() <= 0) {
            return employeeContractIPage;
        }
        List<EmployeeContract> contractList = new ArrayList<>();
        //1、比较结束时间与当前时间，如果结束时间小于当前时间则更改状态
        for (EmployeeContract employeeContract : employeeContractIPage.getRecords()) {
            LocalDateTime contractEndTime = employeeContract.getContractEndTime();
            LocalDateTime now = TimeUtils.getCurrentLocalDateTime();
            if (TimeUtils.compareTwoTime(contractEndTime, now) == -1) {
                employeeContract.setStatus(EmployeeConst.CONTRACT_STATUS_END);
            }
            contractList.add(employeeContract);
        }
        return employeeContractIPage.setRecords(contractList);
    }

    @Override
    public boolean deleteEmployeeContract(EmployeeContract employeeContract) {
        //todo 判断删除的合同上是否有存在有效的协议，如果有存在有效协议，则不允许删除
        employeeContract.setStatus(EmployeeConst.CONTRACT_STATUS_DELETE);
        employeeContract.setContractEndTime(TimeUtils.stringToLocalDateTime("1990-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        return employeeContractService.updateById(employeeContract);
    }

    @Override
    public boolean updateEmployeeContract(EmployeeContract employeeContract) {
        String contractType = employeeContract.getContractType();
        //时间变更协议只允许新增一次
        if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_POSTPONE, contractType)) {
            List<EmployeeContract> contractList = employeeContractService.queryVaildContractByEmpIdAndType(employeeContract.getEmployeeId(), contractType);
            if (contractList.size() > 0) {
                if (contractList.get(0).getId().longValue() != employeeContract.getId().longValue()) {
                    throw new BaseException("时间变更只允许新增一次!", 400001);
                }
            }
        }
        if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_TRAIN, contractType) || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_SECRET, contractType)) {
            employeeContract.setContractStartTime(employeeContract.getContractSignTime());
            employeeContract.setContractEndTime(TimeUtils.stringToLocalDateTime("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }
        return employeeContractService.updateById(employeeContract);
    }

    @Override
    public IPage<EmployeeContract> queryContractByParentId(Long parentContractId, Page<EmployeeContract> page) {
        return employeeContractService.queryContractByParentId(parentContractId, page);
    }

    @Override
    public boolean stopEmployeeContract(EmployeeContract employeeContract) {
        employeeContract.setStatus(EmployeeConst.CONTRACT_STATUS_END);
        return employeeContractService.updateById(employeeContract);
    }
}
