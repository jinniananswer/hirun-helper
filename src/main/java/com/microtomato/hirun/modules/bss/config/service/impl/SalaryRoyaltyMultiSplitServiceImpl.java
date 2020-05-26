package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryRoyaltyMultiSplit;
import com.microtomato.hirun.modules.bss.config.mapper.SalaryRoyaltyMultiSplitMapper;
import com.microtomato.hirun.modules.bss.config.service.ISalaryRoyaltyMultiSplitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 员工工资提成多人拆分比例配置(SalaryRoyaltyMultiSplit)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-26 20:56:16
 */
@Service
@Slf4j
public class SalaryRoyaltyMultiSplitServiceImpl extends ServiceImpl<SalaryRoyaltyMultiSplitMapper, SalaryRoyaltyMultiSplit> implements ISalaryRoyaltyMultiSplitService {

    @Autowired
    private SalaryRoyaltyMultiSplitMapper salaryRoyaltyMultiSplitMapper;

    public SalaryRoyaltyMultiSplit getMultiSplit(String type, String item, Integer num) {
        return this.getOne(Wrappers.<SalaryRoyaltyMultiSplit>lambdaQuery()
                .eq(SalaryRoyaltyMultiSplit::getType, type)
                .eq(SalaryRoyaltyMultiSplit::getItem, item)
                .eq(SalaryRoyaltyMultiSplit::getNum, num)
                .eq(SalaryRoyaltyMultiSplit::getStatus, "U"));
    }

}