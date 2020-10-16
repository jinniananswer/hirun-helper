package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.finance.entity.po.FinanceAcct;
import com.microtomato.hirun.modules.finance.mapper.FinanceAcctMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceAcctService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (FinanceAcct)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-10-13 16:51:29
 */
@Service
@Slf4j
public class FinanceAcctServiceImpl extends ServiceImpl<FinanceAcctMapper, FinanceAcct> implements IFinanceAcctService {

    @Autowired
    private FinanceAcctMapper financeAcctMapper;

    /**
     * 根据付款方式查询付款信息
     * @param paymentId
     * @return
     */
    @Override
    public FinanceAcct getByPaymentId(Long paymentId) {
        return this.getById(paymentId);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Override
    public List<FinanceAcct> queryByType(String type) {
        return this.list(Wrappers.<FinanceAcct>lambdaQuery()
                .eq(FinanceAcct::getType, type)
                .eq(FinanceAcct::getStatus, "U"));
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<FinanceAcct> queryAll() {
        return this.list(Wrappers.<FinanceAcct>lambdaQuery()
                .eq(FinanceAcct::getStatus, "U"));
    }
}