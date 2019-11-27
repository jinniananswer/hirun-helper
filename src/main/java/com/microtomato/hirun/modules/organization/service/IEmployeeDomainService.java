package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.*;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工领域服务接口
 * @author: jinnian
 * @create: 2019-10-08 14:42
 **/
public interface IEmployeeDomainService {

    List<EmployeeInfoDTO> searchEmployee(String searchText);

    EmployeeDTO verifyIdentityNo(String createType, String identityNo, Long employeeId, String operType);

    void verifyMobileNo(String mobileNo, String operType, Long employeeId);

    EmployeeDTO load(Long employeeId);

    Double calculateDiscountRate(Long orgId, String jobRoleNature);

    void employeeEntry(EmployeeDTO employeeDTO);

    boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO);

    IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO employeeInfoDTO, Page<EmployeeQueryConditionDTO> page);

    EmployeeArchiveDTO loadMyArchive(Long employeeId);

    List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO queryConditionDTO);
}
