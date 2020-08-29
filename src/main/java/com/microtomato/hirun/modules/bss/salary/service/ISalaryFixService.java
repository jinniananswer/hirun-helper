package com.microtomato.hirun.modules.bss.salary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryFix;

import java.util.List;

/**
 * 员工固定工资表(SalaryFix)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:26
 */
public interface ISalaryFixService extends IService<SalaryFix> {

    List<SalaryFixDTO> queryEmployeeSalaries(SalaryFixQueryDTO param);

    List<SalaryFixDTO> queryAuditEmployeeSalaries(SalaryFixQueryDTO param);

    List<SalaryFix> queryAllValid();

    List<SalaryFix> queryAllValidAudit();

    void saveSalaries(List<SalaryFixDTO> salaries, boolean isAudit);

    void audit(List<SalaryFixDTO> employeeSalaries, boolean isPass);
}