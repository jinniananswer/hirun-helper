package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.mapper.DecoratorMapper;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 装修工人表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Slf4j
@Service
public class DecoratorServiceImpl extends ServiceImpl<DecoratorMapper, Decorator> implements IDecoratorService {

    @Override
    public List<Decorator> queryDecoratorInfo(Long orgId, Long type) {
        return this.list(new QueryWrapper<Decorator>().lambda()
                .eq(Decorator::getDecoratorType, type)
                .eq(Decorator::getStatus, "0"));
    }


}
