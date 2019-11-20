package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-05
 */
public interface IEmployeeContractService extends IService<EmployeeContract> {
    /**
     * 根据员工ID获取员工合同分页数据
     *
     * @param employeeId
     * @param page
     * @return
     */
    IPage<EmployeeContract> queryEmployeeContracts(Long employeeId, Page<EmployeeContract> page);

    List<EmployeeContract> queryVaildContractByEmpIdAndType(Long employeeId, String contractType);

    /***
     * 根据合同ID查询变更协议
     * @param parentContractId
     * @param page
     * @return
     */
    IPage<EmployeeContract> queryContractByParentId(Long parentContractId, Page<EmployeeContract> page);

    boolean stopEmployeeContract(Long employeeId, LocalDateTime destoryTime,Long userId);
}
