package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.modules.bss.customer.entity.po.BlueprintAction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-28
 */
public interface IBlueprintActionService extends IService<BlueprintAction> {
    /**
     *
     * @param openId
     * @param actionType
     * @return
     */
    List<BlueprintAction> queryBluePrintInfo(String openId, String actionType);

    /**
     * 构建功能蓝图树
     * @param func
     * @param kind
     * @return
     */
    List<TreeNode> buildFuncTree(String func,String kind);
}
