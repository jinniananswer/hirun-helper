package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;

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
    List<EmployeePieStatisticDTO> countBySex();

    /**
     * 按年龄段统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByAge();

    /**
     * 按岗位性质统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByJobRoleNature();

    /**
     * 按司龄统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByCompanyAge();

    /**
     * 按最高学历统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByEducationLevel();

    /**
     * 按在职类型统计员工数量
     * @return
     */
    List<EmployeePieStatisticDTO> countByType();
}