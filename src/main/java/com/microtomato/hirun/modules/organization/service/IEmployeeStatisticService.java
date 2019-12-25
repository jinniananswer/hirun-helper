package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarDTO;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工图表服务接口类
 * @author: jinnian
 * @create: 2019-11-21 16:46
 **/
public interface IEmployeeStatisticService {
    /**
     * 按性别统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countBySex(Long orgId);

    /**
     * 按年龄段统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByAge(Long orgId);

    /**
     * 按岗位性质统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByJobRoleNature(Long orgId);

    /**
     * 按司龄统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByCompanyAge(Long orgId);

    /**
     * 按最高学历统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByEducationLevel(Long orgId);

    /**
     * 按在职类型统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByType(Long orgId);

    /**
     * 统计一年内入职和离职的人数
     * @return
     */
    StatisticBarDTO countInAndDestroyOneYear();
}
