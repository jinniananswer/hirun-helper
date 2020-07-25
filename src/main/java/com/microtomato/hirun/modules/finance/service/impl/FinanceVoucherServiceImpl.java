package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 财务领款单表(FinanceVoucher)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Service
@Slf4j
public class FinanceVoucherServiceImpl extends ServiceImpl<FinanceVoucherMapper, FinanceVoucher> implements IFinanceVoucherService {

    @Autowired
    private FinanceVoucherMapper financeVoucherMapper;
    

}