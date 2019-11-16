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

    /**
     * 根据给定的 用户Id、部门Id、以及岗位，创建对应的 ins_user_role 关联数据。
     *
     * @param userId 用户Id
     * @param orgId 部门Id，对应 ins_org.org_id
     * @param jobRole 岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     */
    @Override
    public void createUserRole(Long userId, Long orgId, String jobRole) {
        // TODO：根据映射表的配置，对应上角色数据。
    }
}
