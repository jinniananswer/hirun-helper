package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;
import com.microtomato.hirun.modules.organization.service.IEmployeeContractDomainService;
import com.microtomato.hirun.modules.organization.service.IEmployeeContractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @DataSource(DataSourceKey.INS)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
            employeeContract.setContractEndTime(TimeUtils.getForeverTime());
        } else if (StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_ROLE, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_PLACE, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_CHANGE_PROBLATION, contractType)
                || StringUtils.equals(EmployeeConst.CONTRACT_TYPE_OTHER, contractType)) {
            //如果变更类型为合同变更设置变更协议的结束时间为当前合同的结束时间，如果未找到则设置时间为2099年
            employeeContract.setContractStartTime(employeeContract.getContractSignTime());

            List<EmployeeContract> timeContracts = employeeContractService.list(new QueryWrapper<EmployeeContract>().lambda()
                    .eq(EmployeeContract::getParentContractId, employeeContract.getParentContractId())
                    .eq(EmployeeContract::getContractType, EmployeeConst.CONTRACT_TYPE_POSTPONE)
                    .gt(EmployeeContract::getContractEndTime, LocalDateTime.now()));

            if (ArrayUtils.isNotEmpty(timeContracts)) {
                EmployeeContract timeContract = timeContracts.get(0);
                employeeContract.setContractEndTime(timeContract.getContractEndTime());
            } else {
                EmployeeContract validContract = employeeContractService.getById(employeeContract.getParentContractId());
                if (validContract == null) {
                    employeeContract.setContractEndTime(TimeUtils.getForeverTime());
                } else {
                    employeeContract.setContractEndTime(validContract.getContractEndTime());
                }
            }
            //如果原来有相应的协议，自动结束
            List<EmployeeContract> existContracts = employeeContractService.list(new QueryWrapper<EmployeeContract>().lambda()
                    .eq(EmployeeContract::getParentContractId, employeeContract.getParentContractId())
                    .eq(EmployeeContract::getContractType, contractType)
                    .gt(EmployeeContract::getContractEndTime, LocalDateTime.now()));

            if(ArrayUtils.isNotEmpty(existContracts)){
                EmployeeContract existContract=existContracts.get(0);
                existContract.setContractEndTime(employeeContract.getContractSignTime());
                this.employeeContractService.updateById(existContract);
            }

        }
        //处理时间由时间变更协议带来的其他协议结束时间变更
        this.modifyEndTimeByPostpone(employeeContract);

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
            employeeContract.setContractEndTime(TimeUtils.getForeverTime());
        }

        //处理时间由时间变更协议带来的其他协议结束时间变更
        this.modifyEndTimeByPostpone(employeeContract);

        return employeeContractService.updateById(employeeContract);
    }

    @Override
    public IPage<EmployeeContract> queryContractByParentId(Long parentContractId, Page<EmployeeContract> page) {

        IPage<EmployeeContract> employeeContractIPage = employeeContractService.queryContractByParentId(parentContractId, page);
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
    public boolean stopEmployeeContract(EmployeeContract employeeContract) {
        employeeContract.setStatus(EmployeeConst.CONTRACT_STATUS_END);
        return employeeContractService.updateById(employeeContract);
    }

    /**
     * 时间变更协议变更，引发其他变更协议结束时间变更
     *
     * @param employeeContract
     */
    private void modifyEndTimeByPostpone(EmployeeContract employeeContract) {
        if (!StringUtils.equals(employeeContract.getContractType(), EmployeeConst.CONTRACT_TYPE_POSTPONE)) {
            return;
        }

        //如果时间变更协议在岗位变更之后，则需要根据时间变更协议时间修改岗位变更协议结束时间
        List<EmployeeContract> changeRoleContracts = employeeContractService.list(new QueryWrapper<EmployeeContract>().lambda()
                .eq(EmployeeContract::getParentContractId, employeeContract.getParentContractId())
                .eq(EmployeeContract::getContractType, EmployeeConst.CONTRACT_TYPE_CHANGE_ROLE)
                .orderByDesc(EmployeeContract::getContractEndTime));

        if (ArrayUtils.isNotEmpty(changeRoleContracts)) {
            EmployeeContract changeRoleContract = changeRoleContracts.get(0);
            changeRoleContract.setContractEndTime(employeeContract.getContractEndTime());
            employeeContractService.updateById(changeRoleContract);
        }

        //如果地点协议在时间变更之之前，则需要根据时间变更协议时间修改地点变更协议结束时间
        List<EmployeeContract> changePlaceContracts = employeeContractService.list(new QueryWrapper<EmployeeContract>().lambda()
                .eq(EmployeeContract::getParentContractId, employeeContract.getParentContractId())
                .eq(EmployeeContract::getContractType, EmployeeConst.CONTRACT_TYPE_CHANGE_PLACE)
                .gt(EmployeeContract::getContractEndTime, LocalDateTime.now()));

        if (ArrayUtils.isNotEmpty(changePlaceContracts)) {
            EmployeeContract changePlaceContract = changePlaceContracts.get(0);
            changePlaceContract.setContractEndTime(employeeContract.getContractEndTime());
            employeeContractService.updateById(changePlaceContract);
        }

        //如果其他协议变更在岗位变更之后，则需要根据时间变更协议时间修改其他变更协议结束时间
        List<EmployeeContract> otherContracts = employeeContractService.list(new QueryWrapper<EmployeeContract>().lambda()
                .eq(EmployeeContract::getParentContractId, employeeContract.getParentContractId())
                .eq(EmployeeContract::getContractType, EmployeeConst.CONTRACT_TYPE_OTHER)
                .gt(EmployeeContract::getContractEndTime, LocalDateTime.now()));

        if (ArrayUtils.isNotEmpty(otherContracts)) {
            EmployeeContract otherContract = otherContracts.get(0);
            otherContract.setContractEndTime(employeeContract.getContractEndTime());
            employeeContractService.updateById(otherContract);
        }

    }
}
