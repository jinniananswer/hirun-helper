package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyStrategy;
import com.microtomato.hirun.modules.bss.config.mapper.SalaryRoyaltyStrategyMapper;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工提成策略配置(SalaryRoyaltyStrategy)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 18:05:14
 */
@Service
@Slf4j
public class SalaryRoyaltyStrategyServiceImpl extends ServiceImpl<SalaryRoyaltyStrategyMapper, SalaryRoyaltyStrategy> implements ISalaryRoyaltyStrategyService {

    @Autowired
    private SalaryRoyaltyStrategyMapper salaryRoyaltyStrategyMapper;

    /**
     * 根据员工ID，角色编码，订单状态查询提成配置策略
     * @param employeeId
     * @param roleId
     * @param orderStatus
     * @return
     */
    @Override
    public List<SalaryRoyaltyStrategy> queryByEmployeeIdRoleIdOrderStatus(Long employeeId, Long roleId, String orderStatus) {
        List<SalaryRoyaltyStrategy> strategies =this.list(new QueryWrapper<SalaryRoyaltyStrategy>().lambda()
            .eq(SalaryRoyaltyStrategy::getStatus, "U")
            .and(v->v.eq(SalaryRoyaltyStrategy::getEmployeeId, employeeId).or().eq(SalaryRoyaltyStrategy::getEmployeeId, -1L))
            .and(v->v.eq(SalaryRoyaltyStrategy::getRoleId, roleId).or().eq(SalaryRoyaltyStrategy::getRoleId, -1L))
            .eq(SalaryRoyaltyStrategy::getOrderStatus, orderStatus));
        return strategies;
    }

    /**
     * 根据员工ID，角色编码，订单状态查询提成配置策略,选出优先级最高的一条，优先级最高的是指定employeeId的
     * @param employeeId
     * @param roleId
     * @param orderStatus
     * @return
     */
    @Override
    public List<SalaryRoyaltyStrategy> getPriorityByEmployeeIdRoleIdOrderStatus(Long employeeId, Long roleId, String orderStatus) {
        List<SalaryRoyaltyStrategy> strategies = this.queryByEmployeeIdRoleIdOrderStatus(employeeId, roleId, orderStatus);

        if (ArrayUtils.isEmpty(strategies)) {
            return null;
        }

        List<SalaryRoyaltyStrategy> result = new ArrayList<>();
        for (SalaryRoyaltyStrategy strategy : strategies) {
            if (employeeId.equals(strategy.getEmployeeId())) {
                result.add(strategy);
            }
        }

        if (ArrayUtils.isNotEmpty(result)) {
            //证明按员工ID已经匹配出数据了，这是最高优先级的，且员工ID与角色ID的配置是冲突的，如果匹配到了员工的，就不要再匹配角色的了
            return result;
        }

        return strategies;
    }

    /**
     * 根据订单状态查询是否有提成计算的配置，有的话需要进行计算
     * @param orderStatus
     * @return
     */
    @Override
    public boolean isNeedCompute(String orderStatus) {
        List<SalaryRoyaltyStrategy> strategies = this.list(Wrappers.<SalaryRoyaltyStrategy>lambdaQuery().
                select(SalaryRoyaltyStrategy::getId).
                eq(SalaryRoyaltyStrategy::getOrderStatus, orderStatus));

        if (ArrayUtils.isEmpty(strategies)) {
            return false;
        } else {
            return true;
        }
    }
}