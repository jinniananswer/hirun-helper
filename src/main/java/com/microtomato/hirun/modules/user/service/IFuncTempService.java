package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-14
 */
public interface IFuncTempService extends IService<FuncTemp> {

    /**
     * 根据用户Id，查临时操作权限
     *
     * @param userId
     * @return
     */
    List<FuncTemp> queryFuncTemp(Long userId);
}
