package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;

import java.util.List;
import java.util.Map;

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
    List<EmployeeInfoDTO> findSubordinate(Long parentEmployeeId);

    /**
     *
     * @param conditionDTO
     * @return
     */
    List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO);

    /**
     * 根据员工id查询员工信息
     * @param employeeId
     * @return
     */
    EmployeeInfoDTO queryEmployeeInfoByEmployeeId(Long employeeId);

    /**
     * 显示生日祝福
     * @return
     */
    Map<String, String> showBirthdayWish(Long employeeId);

    /**
     * 按部门统计员工数量
     * @return
     */
    List<EmployeeQuantityStatDTO> countEmployeeQuantityByOrgId();
}
