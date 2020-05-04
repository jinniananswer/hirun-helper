package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryQueryDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeSalary;

import java.util.List;

/**
 * 员工固定工资表(EmployeeSalary)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-02 00:25:10
 */
public interface IEmployeeSalaryService extends IService<EmployeeSalary> {

    List<EmployeeSalaryDTO> queryEmployeeSalaries(EmployeeSalaryQueryDTO param);

    List<EmployeeSalary> queryByMonth(Integer salaryMonth);

    void saveSalaries(List<EmployeeSalaryDTO> salaries);
}