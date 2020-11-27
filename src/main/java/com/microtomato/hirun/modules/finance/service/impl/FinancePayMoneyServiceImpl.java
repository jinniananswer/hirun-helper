package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayMoney;
import com.microtomato.hirun.modules.finance.mapper.FinancePayMoneyMapper;
import com.microtomato.hirun.modules.finance.service.IFinancePayMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 财务付款方式表(FinancePayMoney)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class FinancePayMoneyServiceImpl extends ServiceImpl<FinancePayMoneyMapper, FinancePayMoney> implements IFinancePayMoneyService {

    @Autowired
    private FinancePayMoneyMapper financePayMoneyMapper;


    /**
     * 根据付款流水查找付款明细
     * @param payNo
     * @return
     */
    @Override
    public List<FinancePayMoney> queryByPayNo(Long payNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<FinancePayMoney>lambdaQuery()
                .eq(FinancePayMoney::getPayNo, payNo)
                .ge(FinancePayMoney::getEndDate, now)
                .le(FinancePayMoney::getStartDate, now));
    }
}