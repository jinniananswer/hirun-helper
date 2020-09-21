package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemStageCfg;
import com.microtomato.hirun.modules.bss.config.mapper.FeeItemStageCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IFeeItemStageCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 费用项分期配置表(FeeItemStageCfg)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-08 00:43:22
 */
@Service
@Slf4j
public class FeeItemStageCfgServiceImpl extends ServiceImpl<FeeItemStageCfgMapper, FeeItemStageCfg> implements IFeeItemStageCfgService {

    @Autowired
    private FeeItemStageCfgMapper feeItemStageCfgMapper;

    /**
     * 根据费用项，订单类型，期数查询费用分期配置信息
     * @param feeItemId
     * @param type
     * @param period
     * @return
     */
    @Override
    public FeeItemStageCfg getByFeeItemIdTypePeriod(Long feeItemId, String type, Integer period) {
        return this.getOne(Wrappers.<FeeItemStageCfg>lambdaQuery()
            .eq(FeeItemStageCfg::getFeeItemId, feeItemId)
            .eq(FeeItemStageCfg::getType, type)
            .eq(FeeItemStageCfg::getPeriods, period));
    }
}