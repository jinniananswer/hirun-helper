package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.user.entity.po.RoleMapping;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.mapper.UserRoleMapper;
import com.microtomato.hirun.modules.user.service.IRoleMappingService;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private IRoleMappingService roleMappingServiceImpl;

    /**
     * 立即生效，结束时间永久。
     *
     * @param userId  用户Id
     * @param orgId   部门Id，对应 ins_org.org_id
     * @param jobRole 岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     */
    @Override
    public void createRole(Long userId, Long orgId, String jobRole) {
        createRole(userId, orgId, jobRole, LocalDateTime.now(), TimeUtils.getForeverTime());
    }

    /**
     * 根据给定的 用户Id、部门Id、以及岗位，创建对应的 ins_user_role 关联数据。
     *
     * 由于存在分公司的情况，每个分公司可能有相同的岗位，为了简化配置，支持 orgId 配置成 0 的情况。
     *
     * @param userId    用户Id
     * @param orgId     部门Id，对应 ins_org.org_id
     * @param jobRole   岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     * @param startDate 生效时间
     * @param endDate   失效时间
     */
    @Override
    public void createRole(Long userId, Long orgId, String jobRole, LocalDateTime startDate, LocalDateTime endDate) {

        RoleMapping one = roleMappingServiceImpl.getOne(
            Wrappers.<RoleMapping>lambdaQuery().select(RoleMapping::getRoleId)
                .eq(RoleMapping::getOrgId, orgId).eq(RoleMapping::getJobRole, jobRole).eq(RoleMapping::getEnabled, true)
        );


        if (null == one) {
            one = roleMappingServiceImpl.getOne(
                Wrappers.<RoleMapping>lambdaQuery().select(RoleMapping::getRoleId)
                    .eq(RoleMapping::getOrgId, 0).eq(RoleMapping::getJobRole, jobRole).eq(RoleMapping::getEnabled, true)
            );
        }

        Assert.notNull(one, String.format("根据 orgId: %s|0, jobRole: %s, 找不到对应的角色！", orgId, jobRole));
        Long roleId = one.getRoleId();

        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setStartDate(startDate);
        userRole.setEndDate(endDate);
        this.save(userRole);
    }

    /**
     * 角色变更，立即终止所有老的角色，并重新创建一个新的角色
     *
     * @param userId 用户Id
     * @param orgId  原部门Id
     */
    public void switchRole(Long userId, Long orgId, String jobRole) {
        deleteAllRole(userId);
        createRole(userId, orgId, jobRole);
    }

    /**
     * 根据 userId, roleId 立即终止其对应的角色
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    @Override
    public void deleteRole(Long userId, Long roleId) {
        deleteRole(userId, roleId, LocalDateTime.now());
    }

    /**
     * 根据 userId, roleId 在指定的时间终止其对应的角色
     *
     * @param userId      用户Id
     * @param roleId      角色Id
     * @param endDatetime 终止时间
     */
    @Override
    public void deleteRole(Long userId, Long roleId, LocalDateTime endDatetime) {
        this.update(Wrappers.<UserRole>lambdaUpdate()
            .set(UserRole::getEndDate, endDatetime)
            .eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId));
    }

    /**
     * 根据 userId 立即终止其对应的所有角色
     *
     * @param userId 用户Id
     */
    @Override
    public void deleteAllRole(Long userId) {
        deleteAllRole(userId, LocalDateTime.now());
    }

    /**
     * 根据 userId 在指定的时间，终止其对应的所有角色
     *
     * @param userId      用户Id
     * @param endDatetime 终止时间
     */
    @Override
    public void deleteAllRole(Long userId, LocalDateTime endDatetime) {
        this.update(Wrappers.<UserRole>lambdaUpdate()
            .set(UserRole::getEndDate, endDatetime)
            .eq(UserRole::getUserId, userId));
    }
}
