package com.microtomato.hirun.modules.organization.entity.domain;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import com.microtomato.hirun.modules.organization.exception.EmployeeException;
import com.microtomato.hirun.modules.organization.service.IEmployeeBlacklistService;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IEmployeeWorkExperienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工领域对象
 * @author: jinnian
 * @create: 2019-10-12 02:44
 **/
@Slf4j
@Component
@Scope("prototype")
public class EmployeeDO {

    private Employee employee;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IEmployeeWorkExperienceService employeeWorkExperienceService;

    @Autowired
    private IEmployeeBlacklistService employeeBlacklistService;;

    /**
     * 设置员工数据
     * @param employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * 根据员工ID设置员工数据
     * @param employeeId
     */
    public void setEmployee(Long employeeId) {
        this.employee = this.employeeService.getById(employeeId);
    }

    /**
     * 根据身份证号码判断该身份证的员工是否存在
     * @return
     */
    public boolean isExists() {
        Employee employee = this.employeeService.queryByIdentityNo(this.employee.getIdentityNo());
        if (employee != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否黑名单员工
     * @return
     */
    public boolean isBlack() {
        return this.employeeBlacklistService.exists(this.employee.getIdentityNo());
    }

    /**
     * 员工新入职
     */
    public void newEntry(EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences) {
        if (this.isExists()) {
            //如果证件号码已存在，则不允许做为新入职录入
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS);
        }

        if (this.isBlack()) {
            //如果是黑名单， 则不予录用
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_BLACK);
        }
        this.employee.setStatus("0");
        this.employeeService.save(employee);

        if (jobRole != null) {
            jobRole.setEmployeeId(employee.getEmployeeId());
            this.allocateJob(jobRole);
        }

        if (ArrayUtils.isNotEmpty(workExperiences)) {
            this.addWorkExperience(employee.getEmployeeId(), workExperiences);
        }
    }

    /**
     * 员工分配工作岗位
     * @param jobRole
     */
    public void allocateJob(EmployeeJobRole jobRole) {
        if (jobRole == null) {
            return;
        }
        jobRole.setIsMain("1");
        jobRole.setStartDate(RequestTimeHolder.getRequestTime());
        jobRole.setEndDate(TimeUtils.getForeverTime());
        this.employeeJobRoleService.save(jobRole);
    }

    /**
     * 添加工作经历
     * @param workExperiences
     */
    public void addWorkExperience(Long employeeId, List<EmployeeWorkExperience> workExperiences) {
        if (ArrayUtils.isEmpty(workExperiences)) {
            return;
        }
        for (EmployeeWorkExperience workExperience : workExperiences) {
            workExperience.setEmployeeId(employeeId);
            this.employeeWorkExperienceService.save(workExperience);
        }
    }

    /**
     * 修改员工基本资料
     */
    public void modify() {

    }

    /**
     * 员工复职
     */
    public void rehire() {

    }

    /**
     * 返聘
     */
    public void rehelloring() {

    }

    /**
     * 注销员工基本资料
     */
    public void destroy(Employee employee, LambdaUpdateWrapper lambdaUpdateWrapper) {
        //更新employee资料
        this.employeeService.updateById(employee);
        //更新employeeJobRole资料
        employeeJobRoleService.update(null,lambdaUpdateWrapper);
    }

    /**
     * 获取该员工的所有下级员工
     * @return
     */
    public List<Employee> findSubordinate() {
        return null;
    }

    /**
     * 获得员工的年纪
     * @return
     */
    public int getAge() {
        LocalDate birthday = this.employee.getBirthday();
        if (birthday == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return TimeUtils.getAbsDateDiffYear(birthday, today);
    }

    /**
     * 获得员工的工作年限
     * @return
     */
    public int getJobYear() {
        LocalDate jobDate = this.employee.getJobDate();
        if (jobDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return TimeUtils.getAbsDateDiffYear(jobDate, today);
    }
}
