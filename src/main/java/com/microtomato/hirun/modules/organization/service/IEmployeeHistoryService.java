package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHistory;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-05
 */
public interface IEmployeeHistoryService extends IService<EmployeeHistory> {

    void createEntry(Long employeeId, LocalDate eventDate);

    void createRehire(Long employeeId, LocalDate eventDate);

    void createRehelloring(Long employeeId, LocalDate eventDate);

    void createChangeJobRole(Long employeeId, EmployeeJobRole newJobRole, LocalDate eventDate);

    void createChangeJobGrade(Long employeeId, String newJobGrade, LocalDate eventDate);

    List<EmployeeHistory> queryHistories(Long employeeId);

    void createTrans(Long employeeId, LocalDate eventDate, String content, String type);

    void createDestroy(Long employeeId, LocalDate eventDate, String type);
}
