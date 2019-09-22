package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.mapper.MenuRoleMapper;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色下挂菜单 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

}
