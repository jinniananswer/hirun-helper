package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.mapper.FuncRoleMapper;
import com.microtomato.hirun.modules.user.service.IFuncRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色权限关系表 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class FuncRoleServiceImpl extends ServiceImpl<FuncRoleMapper, FuncRole> implements IFuncRoleService {

    /**
     * 更新角色对应的操作权限
     *
     * @param roleId 角色Id
     * @param funcIds 操作权限集
     */
    @Override
    public void updateFuncRole(Long roleId, List<Long> funcIds) {
        remove(Wrappers.<FuncRole>lambdaUpdate().eq(FuncRole::getRoleId, roleId));

        Set<Long> longSet = new HashSet();
        longSet.addAll(funcIds);

        for (Long funcId : longSet) {
            FuncRole funcRole = FuncRole.builder().roleId(roleId).funcId(funcId).status("0").build();
            save(funcRole);
        }
    }

}
