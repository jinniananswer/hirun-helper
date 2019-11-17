package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.mapper.UserRoleMapper;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

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
     * @param startDateTime 生效时间
     * @param endDateTime 失效时间
     */
    @Override
    public void createUserRole(Long userId, Long orgId, String jobRole, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // TODO：根据映射表的配置，对应上角色数据。
        // TODO: 如果根据 orgId 和 jobRole 找不到对应的 Role，就不做。
    }

    /**
     * 立即生效，结束时间永久。
     */
    public void createUserRole(Long userId, Long orgId, String jobRole) {
        createUserRole(userId, orgId, jobRole, LocalDateTime.now(), TimeUtils.getForeverTime());
    }

    /**
     * 变更
     *
     * @param userId 用户Id
     * @param orgId 原部门Id
     * @param toOrgId 新部门Id
     */
    public void switchOrg(Long userId, Long orgId, String jobRole) {
        // TODO: 变更就是终止老的所有角色，然后按新增 orgId 和 jobRole 创建新的角色。
        // TODO: orgId 要支持配置 -1 情况，所以映射表里只需配置一条数据，等于根据 jobRole 就要找到对应的角色数据。鸿扬存在分公司的情况，每个分公司都有家装顾问，没得配置多条数据。
    }

    /**
     * 终止的接口
     */

    /**
     * 终止所有的
     */
}
