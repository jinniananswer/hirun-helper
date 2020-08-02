package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceItemMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 财务科目表(FinanceItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
@Service
@Slf4j
public class FinanceItemServiceImpl extends ServiceImpl<FinanceItemMapper, FinanceItem> implements IFinanceItemService {

    @Autowired
    private FinanceItemMapper financeItemMapper;
    

}