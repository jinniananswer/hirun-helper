package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherItemMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 财务领款单明细表(FinanceVoucherItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class FinanceVoucherItemServiceImpl extends ServiceImpl<FinanceVoucherItemMapper, FinanceVoucherItem> implements IFinanceVoucherItemService {

    @Autowired
    private FinanceVoucherItemMapper financeVoucherItemMapper;
    

}