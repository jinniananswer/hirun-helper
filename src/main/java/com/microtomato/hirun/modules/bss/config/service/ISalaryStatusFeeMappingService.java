package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryStatusFeeMapping;
import org.springframework.cache.annotation.Cacheable;

/**
 * 订单状态与费用映射关系配置(SalaryStatusFeeMapping)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-20 17:56:54
 */
public interface ISalaryStatusFeeMappingService extends IService<SalaryStatusFeeMapping> {

    SalaryStatusFeeMapping getStatusFeeMapping(String status);

    @Cacheable("status-fee-mapping-strategy-id")
    SalaryStatusFeeMapping getByStrategyId(Long strategyId);
}