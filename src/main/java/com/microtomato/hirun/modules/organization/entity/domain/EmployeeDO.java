package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.mapper.EmployeeJobRoleMapper;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    /**
     *
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
    public void newEntry(Employee employeeData, EmployeeJobRole jobRole) {
        if (this.isExists()) {
            //如果证件号码已存在，则不允许做为新入职录入
        }

        if (this.isBlack()) {
            //如果是黑名单， 则不予录用
        }
        employeeData.setStatus("0");
        int employeeId = employeeMapper.insert(employeeData);
        this.allocateJob(jobRole);
    }

    public void allocateJob(EmployeeJobRole jobRole) {
        if (jobRole == null) {
            return;
        }
        this.employeeJobRoleMapper.insert(jobRole);
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
    public void destroy() {
    }
}
