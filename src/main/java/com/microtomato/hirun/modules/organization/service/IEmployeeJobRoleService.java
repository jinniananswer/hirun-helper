package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
public interface IEmployeeJobRoleService extends IService<EmployeeJobRole> {

    List<EmployeeJobRole> queryValidMain(Long employeeId);

    EmployeeJobRole queryLast(Long employeeId);
}
