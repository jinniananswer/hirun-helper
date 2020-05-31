package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyStrategy;

import java.util.List;

/**
 * 员工提成策略配置(SalaryRoyaltyStrategy)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 18:05:14
 */
public interface ISalaryRoyaltyStrategyService extends IService<SalaryRoyaltyStrategy> {

    List<SalaryRoyaltyStrategy> queryByEmployeeIdRoleIdOrderStatus(Long employeeId, Long roleId, String orderStatus);

    List<SalaryRoyaltyStrategy> getPriorityByEmployeeIdRoleIdOrderStatus(Long employeeId, Long roleId, String orderStatus);

    boolean isNeedCompute(String orderStatus);
}