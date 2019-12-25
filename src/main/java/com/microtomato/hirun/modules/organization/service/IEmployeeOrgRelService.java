package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeOrgRel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-23
 */
public interface IEmployeeOrgRelService extends IService<EmployeeOrgRel> {

    List<EmployeeOrgRel> queryRelByEmployeeId(Long employeeId);
}
