package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.OrgHrRelInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    IPage<OrgHrRelInfoDTO> queryOrgHrRelList(Long employeeId, Long orgSet, Page<OrgHrRel> page);

    boolean updateOrgHrRel(String id,Long archEmployeeID,Long relationEmployeeId);

    String  getOrgLine(Long employeeId);

    List<OrgHrRel> queryOrgHrRelByEmployeeId(Long employeeId);
}
