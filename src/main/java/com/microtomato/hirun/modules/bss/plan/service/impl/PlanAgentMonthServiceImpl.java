package com.microtomato.hirun.modules.bss.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.plan.mapper.PlanAgentMonthMapper;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (PlanAgentMonth)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:31
 */
@Service
@Slf4j
public class PlanAgentMonthServiceImpl extends ServiceImpl<PlanAgentMonthMapper, PlanAgentMonth> implements IPlanAgentMonthService {

    @Autowired
    private PlanAgentMonthMapper planAgentMonthMapper;
    

}