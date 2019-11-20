package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeChildren;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-19
 */
public interface IEmployeeChildrenService extends IService<EmployeeChildren> {

    List<EmployeeChildren> queryByEmployeeId(Long employeeId);

    void deleteByEmployeeId(Long employeeId);
}
