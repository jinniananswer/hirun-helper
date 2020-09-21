package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustOriginalAction;
import com.microtomato.hirun.modules.bss.customer.mapper.CustOriginalActionMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustOriginalActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户原始动作记录 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
@Slf4j
@Service
public class CustOriginalActionServiceImpl extends ServiceImpl<CustOriginalActionMapper, CustOriginalAction> implements ICustOriginalActionService {

    @Override
    public List<CustOriginalAction> queryOriginalInfo(Long customerId, Long employeeId) {
/*        if(customerId==null || employeeId==null){
            return new ArrayList<CustOriginalAction>();
        }*/
        return this.baseMapper.selectList(new QueryWrapper<CustOriginalAction>().lambda()
                .eq(CustOriginalAction::getCustId,customerId).groupBy(CustOriginalAction::getActionCode));
    }
}
