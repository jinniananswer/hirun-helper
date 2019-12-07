package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.system.entity.po.Func;
import com.microtomato.hirun.modules.system.mapper.FuncMapper;
import com.microtomato.hirun.modules.system.service.IFuncService;
import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.service.IFuncRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class FuncServiceImpl extends ServiceImpl<FuncMapper, Func> implements IFuncService {

    @Autowired
    private IFuncService funcServiceImpl;

    @Autowired
    private IFuncRoleService funcRoleServiceImpl;

    /**
     * 根据角色Id查对应的操作权限编码
     *
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> queryFuncId(Long roleId) {
        Set<Long> rtn = new HashSet<>();

        if (Constants.SUPER_ROLE_ID.equals(roleId)) {
            // 超级管理员有所有的操作权限
            List<Func> list = funcServiceImpl.list(
                Wrappers.<Func>lambdaQuery().select(Func::getFuncId)
            );
            list.forEach(func -> rtn.add(func.getFuncId()));
        } else {
            List<FuncRole> list = funcRoleServiceImpl.list(
                Wrappers.<FuncRole>lambdaQuery()
                    .select(FuncRole::getFuncId)
                    .eq(FuncRole::getStatus, "0")
                    .eq(FuncRole::getRoleId, roleId)
            );
            list.forEach(funcRole -> rtn.add(funcRole.getFuncId()));
        }

        return rtn;
    }
}
