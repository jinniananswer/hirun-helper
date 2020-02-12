package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据员工id判断返回员工是否有下级，以及获取员工离职次数
     * @param employeeId
     * @return
     */
    Map<String,String> queryExtendCondition4Destroy(Long employeeId);

    /**
     * 根据上级员工ID或者部门查询员工
     * @param parentEmployeeId
     * @param orgId
     * @param page
     * @return
     */
    IPage<EmployeeInfoDTO> queryEmployee4BatchChange(Long parentEmployeeId,Long orgId, Page<EmployeeQueryConditionDTO> page);

}
