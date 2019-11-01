package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-10-19
 */
public interface IEmployeeWorkExperienceService extends IService<EmployeeWorkExperience> {

    List<EmployeeWorkExperience> queryByEmployeeId(Long employeeId);

}
