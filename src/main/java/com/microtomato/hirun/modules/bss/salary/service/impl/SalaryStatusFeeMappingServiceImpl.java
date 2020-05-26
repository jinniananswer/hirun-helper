package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryStatusFeeMapping;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryStatusFeeMappingMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryStatusFeeMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态与费用映射关系配置(SalaryStatusFeeMapping)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-20 17:56:54
 */
@Service
@Slf4j
public class SalaryStatusFeeMappingServiceImpl extends ServiceImpl<SalaryStatusFeeMappingMapper, SalaryStatusFeeMapping> implements ISalaryStatusFeeMappingService {

    @Autowired
    private SalaryStatusFeeMappingMapper salaryStatusFeeMappingMapper;

    /**
     * 根据订单状态查询对应需要查询的费用配置
     *
     * @param status
     * @return
     */
    @Override
    public SalaryStatusFeeMapping getStatusFeeMapping(String status) {
        SalaryStatusFeeMapping feeMapping = this.getOne(Wrappers.<SalaryStatusFeeMapping>lambdaQuery()
                .eq(SalaryStatusFeeMapping::getOrderStatus, status));
        return feeMapping;
    }
}