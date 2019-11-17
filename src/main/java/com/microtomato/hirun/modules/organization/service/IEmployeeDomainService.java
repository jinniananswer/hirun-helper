package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeArchiveDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDestroyInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;

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

    EmployeeDTO load(Long employeeId, boolean normal);

    Double calculateDiscountRate(Long orgId, String jobRoleNature);

    void employeeEntry(EmployeeDTO employeeDTO);

    boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO);

    IPage<EmployeeInfoDTO> queryEmployeeList(EmployeeInfoDTO employeeInfoDTO, Page<EmployeeInfoDTO> page);

    EmployeeArchiveDTO loadMyArchive(Long employeeId);
}
