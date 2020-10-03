package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;

import java.time.LocalDate;
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

    /**
     *
     * @param userId
     * @return
     */
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

    /**
     * 根据父级递归查找所有下级员工
     * @param parentEmployeeId
     * @return
     */
    List<EmployeeInfoDTO> recursiveAllSubordinates(String parentEmployeeId);

    /**
     * 加载所有员工
     *
     * @return
     */
    List<Employee> loadEmployee();

    /**
     *
     * @param parentEmployeeId
     * @param orgLine
     * @param employeePage
     * @return
     */
    IPage<EmployeeInfoDTO> queryEmployee4BatchChange(Long parentEmployeeId,String orgLine, Page<EmployeeQueryConditionDTO> employeePage);

    /**
     * 查询员工转正信息
     * @param inDate
     * @param orgLine
     * @param isSign
     * @return
     */
    List<EmployeeInfoDTO> queryEmployeeRegularInfo(LocalDate inDate , String orgLine, String isSign);

    /**
     * 根据某部门获取该部门归属门店或者公司某一角色的员工信息
     * @param orgId
     * @return
     */
    List<SimpleEmployeeDTO> querySimpleEmployeeInfo(Long orgId, Long roleId,Boolean isSelf);

    List<SimpleEmployeeDTO> queryEmployeeBySelectMode(EmployeeSelectDTO select);

    /**
     * 根据某部门查询该部门下的所有员工
      * @param orgId
     * @return
     */
    List<SimpleEmployeeDTO> queryEmployeeByOrgId(Long orgId);

    /**
     *
     * @param orgLine
     * @return
     */
    List<SimpleEmployeeDTO> queryEmployeeByOrgLine(String orgLine);

    /**
     * 根据部门和角色查询员工
     * @param orgId
     * @param roleType
     * @return
     */
    List<SimpleEmployeeDTO> queryEmployeeByRoleAndOrg(Long orgId,String roleType);

    SimpleEmployeeDTO getLoginEmployeeInfo();

    IPage<Employee> queryNewEmployeeByPage(EmployeeQueryDTO employeeQueryDTO, Page<EmployeeQueryDTO> page);

    List<Employee> queryByorgIdAndEmployeeIdAndLikeName(String orgId, Long employeeId, String name);

    Employee getEEmployeeByEmployeeId(Long employeeId);

    /**
     * 获取所有未转正新员工
     * @return
     */
    List<Employee> queryNewEffectiveEmployee();

    List<Employee> queryAllEffectiveEmployee();

    List<Employee> queryEffectiveByJobRoleList(List<String> jobRoleList);

    List<Employee> queryNewEffectiveByJobRoleList(List<String> jobRoleList);
}
