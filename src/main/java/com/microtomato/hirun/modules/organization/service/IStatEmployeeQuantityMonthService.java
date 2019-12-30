package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
public interface IStatEmployeeQuantityMonthService extends IService<StatEmployeeQuantityMonth> {
    /**
     * 查询部门在某年某月的在职人员记录
     *
     * @param year
     * @param month
     * @param orgId
     * @return
     */
    StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId,String jobRole,String jobRoleNature,String orgNature);

    /**
     * 查询部门在职人员数记录
     * @param year
     * @param orgId
     * @return
     */
    List<EmployeeQuantityStatDTO> queryEmployeeQuantityStat(String year, Long orgId);
}
