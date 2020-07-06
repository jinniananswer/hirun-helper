package com.microtomato.hirun.modules.bss.config.service;

import com.microtomato.hirun.modules.bss.config.entity.po.Action;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动作定义表 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-27
 */
public interface IActionService extends IService<Action> {
    /**
     *
     * @param actionType
     * @return
     */
    List<Action> queryActions(String actionType);
}
