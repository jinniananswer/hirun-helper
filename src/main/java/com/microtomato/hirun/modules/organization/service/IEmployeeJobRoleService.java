package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeOrgGroupByDTO;
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

    EmployeeJobRole queryValidMain(Long employeeId);

    List<EmployeeJobRole> queryAll(Long employeeId);

    EmployeeJobRole queryLast(Long employeeId);

    boolean changeParentEmployee(Long oldParentEmployeeId,Long newParentEmployeeId,Long userId);

    List<EmployeeOrgGroupByDTO> countGroupByOrgId();

    /**
     * 批量修改员工上级
     * @param ids
     * @param parentEmployeeId
     * @return
     */
    boolean batchUpdateParentEmployee(String ids,Long parentEmployeeId);

    List<EmployeeJobRole> queryEffectiveByOrgIdList(List<Long> orgIdList);
}
