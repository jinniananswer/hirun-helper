package com.microtomato.hirun.modules.organization.entity.domain;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import com.microtomato.hirun.modules.organization.mapper.EmployeeJobRoleMapper;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.mapper.EmployeeWorkExperienceMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeWorkExperienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Wrapper;
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

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeJobRoleMapper employeeJobRoleMapper;

    @Autowired
    private EmployeeWorkExperienceMapper employeeWorkExperienceMapper;

    @Autowired
    private IEmployeeWorkExperienceService employeeWorkExperienceService;

    /**
     * 根据身份证号码判断该身份证的员工是否存在
     * @return
     */
    public boolean isExists() {
        return false;
    }

    /**
     * 是否黑名单员工
     * @return
     */
    public boolean isBlack() {
        return false;
    }

    /**
     * 员工新入职
     */
    public void newEntry(Employee employeeData, EmployeeJobRole jobRole, List<EmployeeWorkExperience> workExperiences) {
        if (this.isExists()) {
            //如果证件号码已存在，则不允许做为新入职录入
        }

        if (this.isBlack()) {
            //如果是黑名单， 则不予录用
        }
        employeeData.setStatus("0");
        employeeMapper.insert(employeeData);

        if (jobRole != null) {
            jobRole.setEmployeeId(employeeData.getEmployeeId());
            this.allocateJob(jobRole);
        }

        if (ArrayUtils.isNotEmpty(workExperiences)) {
            this.addWorkExperience(employeeData.getEmployeeId(), workExperiences);
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
        this.employeeJobRoleMapper.insert(jobRole);
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
            employeeWorkExperienceMapper.insert(workExperience);
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
        employeeMapper.updateById(employee);
        //更新employeeJobRole资料
        employeeJobRoleMapper.update(null,lambdaUpdateWrapper);
    }
}
