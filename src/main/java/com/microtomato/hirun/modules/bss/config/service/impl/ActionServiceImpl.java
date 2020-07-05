package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.config.entity.po.Action;
import com.microtomato.hirun.modules.bss.config.mapper.ActionMapper;
import com.microtomato.hirun.modules.bss.config.service.IActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 动作定义表 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-27
 */
@Slf4j
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements IActionService {

    @Override
    public List<Action> queryActions(String actionType) {
        return this.baseMapper.selectList(new QueryWrapper<Action>().lambda()
                .eq(Action::getActionType,actionType)
                .eq(Action::getStatus,"1")
                .orderByAsc(Action::getOrderNo));
    }
}
