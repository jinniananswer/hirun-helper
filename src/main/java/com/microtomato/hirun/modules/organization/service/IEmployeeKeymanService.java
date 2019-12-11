package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeKeyman;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-10
 */
public interface IEmployeeKeymanService extends IService<EmployeeKeyman> {

    List<EmployeeKeyman> queryByEmployeeId(Long employeeId);

    void deleteByEmployeeId(Long employeeId, String type);

    void deleteByEmployeeId(Long employeeId);
}
