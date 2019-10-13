package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
public interface IEmployeeJobRoleService extends IService<EmployeeJobRole> {

    EmployeeJobRole getValidJobRole(Integer employeeId);
}
