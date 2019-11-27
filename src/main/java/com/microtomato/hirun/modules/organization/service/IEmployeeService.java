package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryConditionDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
public interface IEmployeeService extends IService<Employee> {

    Employee queryByIdentityNo(String identityNo);

    IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO employeeInfoDTO, Page<EmployeeQueryConditionDTO> employeePage);

    /**
     * 测试（后期删除）
     */
    IPage<EmployeeExampleDTO> selectEmployeePageExample(String name, Long orgId, Long jobRole);

    /**
     * 根据employeeId获取name
     */
    String getEmployeeNameEmployeeId(Long employeeId);

    Employee queryByUserId(Long userId);

    /**
     * 查找下级员工
     * @param parentEmployeeId
     * @return
     */
    List<Employee> findSubordinate(Long parentEmployeeId);

    /**
     *
     * @param employeeInfoDTO
     * @return
     */
    List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO);
}
