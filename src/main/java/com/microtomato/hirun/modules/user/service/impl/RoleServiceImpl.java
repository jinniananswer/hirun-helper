package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.modules.user.entity.po.Role;
import com.microtomato.hirun.modules.user.mapper.RoleMapper;
import com.microtomato.hirun.modules.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色表
(归属组织的角色，职级的角色等) 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
