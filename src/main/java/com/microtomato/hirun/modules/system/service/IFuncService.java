package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.Func;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
public interface IFuncService extends IService<Func> {

    /**
     * 根据角色Id查对应的操作权限编码
     *
     * @param roleId
     * @return
     */
    Set<Long> queryFuncId(Long roleId);


    /**
     * 查操作权限
     *
     * @param type
     * @return
     */
    List<Func> queryFuncList(String type);
}
