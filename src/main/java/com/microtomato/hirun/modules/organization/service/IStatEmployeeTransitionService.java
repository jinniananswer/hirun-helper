package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransitionStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeTransition;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

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
     * 查询异动信息记录
     *
     * @param orgId
     * @param localDate
     * @return
     */
    StatEmployeeTransition queryTransitionRecord(Long orgId, LocalDate localDate);

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
    void addEmployeeTransTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate transDate);

    /**
     * 新增员工借调异动
     *
     * @param inOrgId
     * @param outOrgId
     * @param employeeId
     * @param borrowDate
     */
    void addEmployeeBorrowTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate borrowDate);

    /**
     * 新增员工销户异动
     *
     * @param orgId
     * @param employeeId
     * @param destroyDate
     */
    void addEmployeeDestroyTransition(Long orgId, Long employeeId, LocalDate destroyDate);

    /**
     * 查询异动list
     *
     * @param orgId
     * @param queryTime
     * @return
     */
    List<EmployeeTransitionStatDTO> queryTransitionList(Long orgId, String queryTime);
}
