package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<EmployeeContract> list=employeeContractService.queryVaildContractByEmpIdAndType(employeeContract.getEmployeeId(),employeeContract.getContractType());
        if(ArrayUtils.isNotEmpty(list)){
            throw new BaseException("已存在相同类型的合同/协议，请检查!",4000001);
        }
        employeeContract.setStatus("1");
        employeeContractService.save(employeeContract);
    }

    @Override
    public IPage<EmployeeContract> queryEmployeeContracts(Long employeeId, Page<EmployeeContract> page) {
        return employeeContractService.queryEmployeeContracts(employeeId,page);
    }

    @Override
    public boolean deleteEmployeeContract(EmployeeContract employeeContract) {
        //todo 判断删除的合同上是否有存在有效的协议，如果有存在有效协议，则不允许删除
        employeeContract.setStatus("2");
        return employeeContractService.updateById(employeeContract);
    }

    @Override
    public boolean updateEmployeeContract(EmployeeContract employeeContract) {
        return employeeContractService.updateById(employeeContract);
    }
}
