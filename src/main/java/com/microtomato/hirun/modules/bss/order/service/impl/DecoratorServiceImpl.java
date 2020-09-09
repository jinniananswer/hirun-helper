package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.mapper.DecoratorMapper;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DecoratorMapper decoratorMapper;

    @Override
    public List<Decorator> queryDecoratorInfo(Long orgId, Long type) {
        return this.list(new QueryWrapper<Decorator>().lambda()
                .eq(Decorator::getDecoratorType, type)
                .eq(Decorator::getStatus, "0"));
    }

    @Override
    public IPage<Decorator> queryDecoratorInfo(String name, String identityNo, String decoratorType, int current, int size) {
        LambdaQueryWrapper<Decorator> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(name), Decorator::getName, name)
                .eq(StringUtils.isNotEmpty(identityNo), Decorator::getIdentityNo, identityNo)
                .eq(Decorator::getStatus, "0");
        Page<Decorator> page = new Page<>(current, size);
        IPage<Decorator> decoratorPages = decoratorMapper.selectPage(page, lambdaQueryWrapper);

        return decoratorPages;
    }

    @Override
    public List<Decorator> queryAllInfo() {
        return this.list(new QueryWrapper<Decorator>().lambda()
                .eq(Decorator::getStatus, "0"));
    }

    /**
     * 根据工人ID查询工人信息
     *
     * @param decoratorId
     * @return
     */
    @Override
    public Decorator getDecorator(Long decoratorId) {
        Decorator decorator = this.getById(decoratorId);
        return decorator;
    }

    /**
     * 根据工人姓名、证件号码查询工人信息
     *
     * @param name
     * @param identityNo
     * @return
     */
    @Override
    public Decorator getDecorator(String name, String identityNo) {
        Decorator decorator = this.getOne(new QueryWrapper<Decorator>().lambda()
                .eq(Decorator::getName, name)
                .eq(Decorator::getIdentityNo, identityNo));

        return decorator;
    }
}
