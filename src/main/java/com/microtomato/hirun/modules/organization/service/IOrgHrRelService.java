package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-27
 */
public interface IOrgHrRelService extends IService<OrgHrRel> {
    /**
     *
     * @param orgId
     * @return
     */
    OrgHrRel queryValidQrgHrRel(Long orgId);

    Employee queryValidRemindEmployeeId(String employeeType, Long orgId);
}
