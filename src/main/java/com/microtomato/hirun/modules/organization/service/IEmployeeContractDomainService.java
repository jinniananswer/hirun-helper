package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;


/**
 * @program: hirun-helper
 * @description: 员工合同领域服务接口
 * @author: liuhui
 **/
public interface IEmployeeContractDomainService {
    /**
     * 新建员工合同
     * @param employeeContract
     */
    void createEmployeeContract(EmployeeContract employeeContract);

    /**
     * 查询员工合同信息
     */
    IPage<EmployeeContract> queryEmployeeContracts(Long employeeId,Page<EmployeeContract> page);

    /**
     * 删除合同
     * @param employeeContract
     */
    boolean deleteEmployeeContract(EmployeeContract employeeContract);

    /**
     * 修改合同
     */
    boolean updateEmployeeContract(EmployeeContract employeeContract);

    /**
     * 根据合同id查询变更协议
     */
    IPage<EmployeeContract> queryContractByParentId(Long parentContractId,Page<EmployeeContract> page);

    /**
     * 合同终止
     */
    boolean stopEmployeeContract(EmployeeContract employeeContract);
}
