package com.microtomato.hirun.modules.bss.salary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.CreateEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.QueryEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRedebit;

import java.util.List;

/**
 * 员工补扣款信息表(SalaryRedebit)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-30 20:34:51
 */
public interface ISalaryRedebitService extends IService<SalaryRedebit> {

    IPage<EmployeeRedebitDTO> queryEmployeeRedebits(QueryEmployeeRedebitDTO condition);

    void createRedebit(CreateEmployeeRedebitDTO employeeRedebit);

    void deleteRedebits(List<EmployeeRedebitDTO> redebits);

    void auditRedebit(List<EmployeeRedebitDTO> redebits, boolean pass);
}