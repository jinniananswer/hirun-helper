package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.modules.bss.customer.entity.po.ProjectOriginalAction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户原始动作记录 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
public interface IProjectOriginalActionService extends IService<ProjectOriginalAction> {
    /**
     * 查询客户代表动作完成情况
     * @param partyId
     * @return
     */
    List<ProjectOriginalAction> queryOriginalActionInfo(Long partyId);

}
