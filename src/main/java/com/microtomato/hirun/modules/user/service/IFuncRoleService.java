package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限关系表 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
public interface IFuncRoleService extends IService<FuncRole> {

    /**
     * 更新角色对应的操作权限
     *
     * @param roleId 角色Id
     * @param funcIds 操作权限集
     */
    void updateFuncRole(Long roleId, List<Long> funcIds);
}
