package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransitonDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeTransition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-22
 */
public interface IStatEmployeeTransitionService extends IService<StatEmployeeTransition> {

    /**
     * 新增员工入职异动
     *
     * @param orgId
     * @param employeeId
     */
    void addEmployeeEntryTransition(Long orgId, Long employeeId, LocalDate inDate);

    /**
     * 新增员工休假异动
     *
     * @param orgId
     * @param employeeId
     * @param holidayDate
     */
    void addEmployeeHolidayTransition(Long orgId, Long employeeId, LocalDate holidayDate);

    /**
     * 新增员工调出异动
     *
     * @param inOrgId
     * @param outOrgId
     * @param employeeId
     * @param transDate
     */
    void addEmployeeTransTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate transDate,String oldJobRole,String oldJobRoleNature);

    /**
     * 新增员工借调异动
     *
     * @param inOrgId
     * @param outOrgId
     * @param employeeId
     * @param borrowDate
     */
    void addEmployeeBorrowTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate borrowDate,String oldJobRole,String oldJobRoleNature);

    /**
     * 新增员工销户异动
     *
     * @param dto
     */
    void addEmployeeDestroyTransition(EmployeeTransitonDTO dto);

    /**
     * 查询异动list
     *
     * @param orgId
     * @param queryTime
     * @return
     */
    List<Map<String,String>> queryTransitionList(Long orgId, String queryTime);
}
