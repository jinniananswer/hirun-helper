package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.mapper.UserRoleMapper;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
