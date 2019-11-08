package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;
import com.microtomato.hirun.modules.organization.mapper.EmployeeContractMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-05
 */
@Slf4j
@Service
public class EmployeeContractServiceImpl extends ServiceImpl<EmployeeContractMapper, EmployeeContract> implements IEmployeeContractService {

    @Autowired
    EmployeeContractMapper contractMapper;

    @Override
    public IPage<EmployeeContract> queryEmployeeContracts(Long employeeId, Page<EmployeeContract> page) {
        QueryWrapper<EmployeeContract> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("employee_id",employeeId);
        IPage<EmployeeContract> iPage=this.contractMapper.selectPage(page,queryWrapper);
        log.debug(iPage.getTotal()+"");
        return iPage;
    }

    @Override
    public List<EmployeeContract> queryVaildContractByEmpIdAndType(Long employeeId, String contractType) {
        QueryWrapper<EmployeeContract> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("employee_id",employeeId);
        queryWrapper.eq("contract_type",contractType);
        queryWrapper.eq("status","1");
        queryWrapper.apply("contract_start_time < contract_end_time ");
        return this.contractMapper.selectList(queryWrapper);
    }
}
