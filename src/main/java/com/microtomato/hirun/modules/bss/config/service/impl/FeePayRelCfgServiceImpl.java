package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.FeePayRelCfg;
import com.microtomato.hirun.modules.bss.config.mapper.FeePayRelCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IFeePayRelCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用项与收款项配置表(FeePayRelCfg)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-05 00:12:28
 */
@Service
@Slf4j
public class FeePayRelCfgServiceImpl extends ServiceImpl<FeePayRelCfgMapper, FeePayRelCfg> implements IFeePayRelCfgService {

    @Autowired
    private FeePayRelCfgMapper feePayRelCfgMapper;

    /**
     * 查询所有费用项与付款项关系
     * @return
     */
    @Override
    @Cacheable(value = "sys_fee_pay_rel_cfg-all")
    public List<FeePayRelCfg> queryAll() {
        return this.list(new QueryWrapper<FeePayRelCfg>().lambda().eq(FeePayRelCfg::getStatus, "U"));
    }

    /**
     * 根据费用项ID列表查询费用项与付款项关系数据
     * @param feeItemIds
     * @return
     */
    @Override
    @Cacheable(value = "sys_fee_pay_rel_cfg-feeItemIds")
    public List<FeePayRelCfg> queryByFeeItemIds(List<Long> feeItemIds) {
        List<FeePayRelCfg> payRelCfgs = this.queryAll();
        if (ArrayUtils.isEmpty(payRelCfgs)) {
            return null;
        }

        List<FeePayRelCfg> result = new ArrayList<>();
        for (Long feeItemId : feeItemIds) {
            for (FeePayRelCfg payRelCfg : payRelCfgs) {
                if (feeItemId.equals(payRelCfg.getFeeItemId())) {
                    result.add(payRelCfg);
                }
            }
        }

        return result;
    }

    /**
     * 根据付款项找到费用项关系
     * @param payItemId
     * @return
     */
    @Override
    @Cacheable(value = "sys_fee_pay_rel_cfg-payItemId")
    public FeePayRelCfg getByPayItemId(Long payItemId) {
        return this.getOne(Wrappers.<FeePayRelCfg>lambdaQuery()
                .eq(FeePayRelCfg::getPayItemId, payItemId)
                .eq(FeePayRelCfg::getStatus, "U"), false);
    }
}