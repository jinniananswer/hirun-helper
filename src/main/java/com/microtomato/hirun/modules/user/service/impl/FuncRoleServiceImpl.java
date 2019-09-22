package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.modules.user.entity.po.FuncRole;
import com.microtomato.hirun.modules.user.mapper.FuncRoleMapper;
import com.microtomato.hirun.modules.user.service.IFuncRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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

}
