package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeChildren;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import com.microtomato.hirun.modules.organization.exception.EmployeeException;
import com.microtomato.hirun.modules.organization.service.*;
import com.microtomato.hirun.modules.organization.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private IEmployeeBlacklistService employeeBlacklistService;

    @Autowired
    private IEmployeeHistoryService employeeHistoryService;

    @Autowired
    private IEmployeeChildrenService employeeChildrenService;

    /**
     * 默认构造函数
     */
    public EmployeeDO() {

    }

    /**
     * 以employeeId构造领域对象
     * @param employeeId
     */
    public EmployeeDO(Long employeeId) {
        IEmployeeService service = SpringContextUtils.getBean(EmployeeServiceImpl.class);
        this.employee = service.getById(employeeId);
        if (this.employee == null) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.NOT_FOUND);
        }
    }

    /**
     * 以员工数据对象为基础构造领域对象
     * @param employee
     */
    public EmployeeDO(Employee employee) {
        this.employee = employee;
    }

    /**
     * 获取员工数据对象
     * @return
     */
    public Employee getEmployee() {
        return this.employee;
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
    public void newEntry(EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences, List<EmployeeChildren> children) {
        if (this.isExists()) {
            //如果证件号码已存在，则不允许做为新入职录入
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "证件号码", this.employee.getName(), this.employee.getMobileNo());
        }

        if (this.isBlack()) {
            //如果是黑名单， 则不予录用
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_BLACK);
        }
        this.employee.setStatus(EmployeeConst.STATUS_NORMAL);
        this.employeeService.save(employee);

        if (jobRole != null) {
            jobRole.setEmployeeId(employee.getEmployeeId());
            this.allocateJob(jobRole);
        }

        if (ArrayUtils.isNotEmpty(workExperiences)) {
            this.addWorkExperience(workExperiences);
        }

        if (ArrayUtils.isNotEmpty(children)) {
            this.addChildren(children);
        }

        this.employeeHistoryService.createEntry(this.employee.getEmployeeId(), this.employee.getInDate().toLocalDate());
    }

    /**
     *
     * @param employee 修改后的员工数据
     * @param jobRole
     * @param workExperiences
     */
    public void modify(Employee employee, EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences, List<EmployeeChildren> children) {
        if (!employee.getEmployeeId().equals(this.employee.getEmployeeId())) {
            //员工ID不相等，表示不是修改的同一员工数据
            return;
        }
        employee.setStatus(EmployeeConst.STATUS_NORMAL);
        this.employeeService.updateById(employee);

        if (jobRole != null) {
            //终止原有有效的jobRole数据
            this.destroyJob(null);
            //新分配岗位
            jobRole.setEmployeeId(employee.getEmployeeId());
            this.allocateJob(jobRole);
        }

        if (ArrayUtils.isNotEmpty(workExperiences)) {
            this.deleteWorkExperiences();
            this.addWorkExperience(workExperiences);
        }

        if (ArrayUtils.isNotEmpty(children)) {
            this.deleteChildren();
            this.addChildren(children);
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
        jobRole.setIsMain(EmployeeConst.JOB_ROLE_MAIN);
        jobRole.setStartDate(RequestTimeHolder.getRequestTime());
        jobRole.setEndDate(TimeUtils.getForeverTime());
        this.employeeJobRoleService.save(jobRole);
    }

    /**
     * 终止员工的工作岗位数据
     */
    public void destroyJob(LocalDateTime endDate) {
        List<EmployeeJobRole> oldJobRoles = this.employeeJobRoleService.queryAll(this.employee.getEmployeeId());
        if (ArrayUtils.isNotEmpty(oldJobRoles)) {
            for (EmployeeJobRole oldJobRole : oldJobRoles) {
                if (endDate != null) {
                    oldJobRole.setEndDate(endDate);
                } else {
                    oldJobRole.setEndDate(RequestTimeHolder.getRequestTime());
                }
                this.employeeJobRoleService.updateById(oldJobRole);
            }
        }
    }

    /**
     * 删除员工的入职前工作经历数据
     */
    public void deleteWorkExperiences() {
        List<EmployeeWorkExperience> workExperiences = this.employeeWorkExperienceService.queryByEmployeeId(this.employee.getEmployeeId());
        if (ArrayUtils.isNotEmpty(workExperiences)) {
            for (EmployeeWorkExperience workExperience : workExperiences) {
                this.employeeWorkExperienceService.removeById(workExperience.getId());
            }
        }
    }

    /**
     * 添加工作经历
     * @param workExperiences
     */
    public void addWorkExperience(List<EmployeeWorkExperience> workExperiences) {
        if (ArrayUtils.isEmpty(workExperiences)) {
            return;
        }
        for (EmployeeWorkExperience workExperience : workExperiences) {
            workExperience.setEmployeeId(this.employee.getEmployeeId());
            this.employeeWorkExperienceService.save(workExperience);
        }

    }

    /**
     * 添加员工子女信息
     * @param children
     */
    public void addChildren(List<EmployeeChildren> children) {
        if (ArrayUtils.isEmpty(children)) {
            return;
        }

        for (EmployeeChildren child : children) {
            child.setEmployeeId(this.employee.getEmployeeId());
            this.employeeChildrenService.save(child);
        }
    }

    /**
     * 删除员工子女信息
     */
    public void deleteChildren() {
        this.employeeChildrenService.deleteByEmployeeId(this.employee.getEmployeeId());
    }

    /**
     * 员工复职
     */
    public void rehire(Employee employee, EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences, List<EmployeeChildren> children) {
        this.modify(employee, jobRole, workExperiences, children);
        this.employeeHistoryService.createRehire(employee.getEmployeeId(), employee.getInDate().toLocalDate());
    }

    /**
     * 返聘
     */
    public void rehelloring(Employee employee, EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences, List<EmployeeChildren> children) {
        this.modify(employee, jobRole, workExperiences, children);
        this.employeeHistoryService.createRehelloring(employee.getEmployeeId(), employee.getInDate().toLocalDate());
    }

    /**
     * 注销员工基本资料
     */
    public void destroy(LocalDateTime destoryTime) {
        //终止employee
        if(StringUtils.equals(this.employee.getDestroyWay(),EmployeeConst.DESTORY_WAY_RETIRE)){
            this.employee.setStatus(EmployeeConst.STATUS_RETIRE);
        }else{
            this.employee.setStatus(EmployeeConst.STATUS_DESTROY);
        }
        this.employeeService.updateById(employee);
        //终止jobrole
        this.destroyJob(destoryTime);
    }

    /**
     * 获取该员工的所有下级员工
     * @return
     */
    public List<Employee> findSubordinate() {
        List<Employee> childEmployees=employeeService.findSubordinate(this.employee.getEmployeeId());
        return childEmployees;
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

    /**
     * 判断今天是否员工生日
     * @return
     */
    public boolean isTodayBirthday() {
        LocalDate birthday = this.employee.getBirthday();
        if (birthday == null) {
            return false;
        }

        LocalDate now = LocalDate.now();
        if (birthday.getMonthValue() == now.getMonthValue() && birthday.getDayOfMonth() == now.getDayOfMonth()) {
            return true;
        }
        return false;
    }
}
