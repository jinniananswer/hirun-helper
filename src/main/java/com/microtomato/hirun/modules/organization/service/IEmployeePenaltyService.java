package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePenaltyDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePenalty;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-14
 */
public interface IEmployeePenaltyService extends IService<EmployeePenalty> {
    /**
     * 新增奖惩
     * @param employeePenalty
     */
    void addEmployeePenalty(EmployeePenalty employeePenalty);

    /**
     * 查询奖惩信息
     * @param employeePenaltyDTO
     * @return
     */
    IPage<EmployeePenaltyDTO> queryPenaltyList(EmployeePenaltyDTO employeePenaltyDTO, Page<EmployeePenaltyDTO> page);

    /**
     * 删除奖惩
     * @param employeePenalty
     */
    boolean deleteEmployeePenalty(EmployeePenalty employeePenalty);

    /**
     * 修改奖惩
     * @param employeePenalty
     * @return
     */
    boolean updateEmployeePenalty(EmployeePenalty employeePenalty);


}
