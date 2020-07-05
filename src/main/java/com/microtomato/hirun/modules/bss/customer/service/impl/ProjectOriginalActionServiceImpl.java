package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.customer.entity.po.ProjectOriginalAction;
import com.microtomato.hirun.modules.bss.customer.mapper.ProjectOriginalActionMapper;
import com.microtomato.hirun.modules.bss.customer.service.IProjectOriginalActionService;
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
public class ProjectOriginalActionServiceImpl extends ServiceImpl<ProjectOriginalActionMapper, ProjectOriginalAction> implements IProjectOriginalActionService {
    @Override
    public List<ProjectOriginalAction> queryOriginalActionInfo(Long partyId) {
        if (partyId == null) {
            return new ArrayList<ProjectOriginalAction>();
        }
        return this.baseMapper.selectList(new QueryWrapper<ProjectOriginalAction>().lambda()
                .eq(ProjectOriginalAction::getPartyId, partyId).eq(ProjectOriginalAction::getStatus,"1"));
    }
}
