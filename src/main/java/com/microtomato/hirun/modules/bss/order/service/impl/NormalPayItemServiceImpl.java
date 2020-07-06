package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayItemMapper;
import com.microtomato.hirun.modules.bss.order.service.INormalPayItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 非主营支付项明细表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-30
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class NormalPayItemServiceImpl extends ServiceImpl<NormalPayItemMapper, NormalPayItem> implements INormalPayItemService {

    /**
     * 根据\支付流水查询非主营支付项信息
     *
     * @param payNo
     * @return
     */
    @Override
    public List<NormalPayItem> queryByPayNo(Long payNo) {
        return this.list(new QueryWrapper<NormalPayItem>().lambda().eq(NormalPayItem::getPayNo, payNo).gt(NormalPayItem::getEndDate, RequestTimeHolder.getRequestTime()));
    }
}
