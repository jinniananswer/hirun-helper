package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.user.entity.po.RoleMapping;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.microtomato.hirun.modules.user.mapper.UserRoleMapper;
import com.microtomato.hirun.modules.user.service.IRoleMappingService;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    private IRoleMappingService roleMappingService;

    /**
     * 根据 User 查 UserRole
     *
     * @param user
     * @return
     */
    @Override
    public List<UserRole> queryUserRole(User user) {
        LocalDateTime now = LocalDateTime.now();
        return list(
            Wrappers.<UserRole>lambdaQuery()
                .select(UserRole::getRoleId, UserRole::getIsMainRole)
                .eq(UserRole::getUserId, user.getUserId())
                .lt(UserRole::getStartDate, now)
                .gt(UserRole::getEndDate, now)
        );
    }

    /**
     * 立即生效，结束时间永久。
     *
     * @param userId  用户Id
     * @param orgId   部门Id，对应 ins_org.org_id
     * @param jobRole 岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     */
    @Override
    public void createRole(Long userId, Long orgId, String jobRole, String orgNature) {
        createRole(userId, orgId, jobRole, orgNature, LocalDateTime.now(), TimeUtils.getForeverTime());
    }

    /**
     * 根据给定的 用户Id、部门Id、以及岗位，创建对应的 ins_user_role 关联数据。
     * <p>
     * 由于存在分公司的情况，每个分公司可能有相同的岗位，为了简化配置，支持 orgId 配置成 0 的情况。
     *
     * @param userId    用户Id
     * @param orgId     部门Id，对应 ins_org.org_id
     * @param jobRole   岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     * @param orgNature 部门性质
     * @param startDate 生效时间
     * @param endDate   失效时间
     */
    @Override
    public void createRole(Long userId, Long orgId, String jobRole, String orgNature, LocalDateTime startDate, LocalDateTime endDate) {

        RoleMapping one = roleMappingService.getOne(
            Wrappers.<RoleMapping>lambdaQuery()
                .select(RoleMapping::getRoleId)
                .eq(RoleMapping::getJobRole, jobRole)
                .and(v -> v.eq(RoleMapping::getOrgId, orgId).or().eq(RoleMapping::getOrgId, 0))
                .and(v -> v.eq(RoleMapping::getOrgNature, orgNature).or().isNull(RoleMapping::getOrgNature))
                .eq(RoleMapping::getEnabled, true)
        );

        if (null == one) {
            log.info("根据 orgId: {}|0, jobRole: {}, 找不到对应的角色！", orgId, jobRole);
            // 如果找不到对应的角色信息，直接返回，不阻断新建客户资料流程。
            return;
        }

        Long roleId = one.getRoleId();
        UserRole userRole = UserRole.builder().userId(userId).roleId(roleId).startDate(startDate).endDate(endDate).build();
        this.save(userRole);

    }

    /**
     * 角色变更，立即终止所有老的角色，并重新创建一个新的角色
     *
     * @param userId 用户Id
     * @param orgId  原部门Id
     * @param jobRole 岗位
     * @param orgNature 部门性质
     */
    @Override
    public void switchRole(Long userId, Long orgId, String jobRole, String orgNature) {
        deleteAllRole(userId);
        createRole(userId, orgId, jobRole, orgNature);
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
        this.update(
            Wrappers.<UserRole>lambdaUpdate()
                .set(UserRole::getEndDate, endDatetime)
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId)
        );
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
        this.update(
            Wrappers.<UserRole>lambdaUpdate()
                .set(UserRole::getEndDate, endDatetime)
                .eq(UserRole::getUserId, userId)
        );
    }

    /**
     * 批量分配角色
     *
     * @param userIds
     * @param roleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void grantRole(List<Long> userIds, List<Long> roleIds) {

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        LocalDateTime foreverTime = TimeUtils.getForeverTime();

        for (Long userId : userIds) {
            for (Long roleId : roleIds) {

                UserRole one = this.getOne(
                    Wrappers.<UserRole>lambdaQuery()
                        .eq(UserRole::getUserId, userId)
                        .eq(UserRole::getRoleId, roleId)
                        .lt(UserRole::getStartDate, now)
                        .gt(UserRole::getEndDate, now)
                        .last("LIMIT 1")
                );

                if (null == one) {
                    UserRole userRole = UserRole.builder().userId(userId).roleId(roleId).startDate(now).endDate(foreverTime).build();
                    this.save(userRole);
                }

            }
        }

    }

    /**
     * 批量回收角色
     *
     * @param userIds
     * @param roleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void revokeRole(List<Long> userIds, List<Long> roleIds) {
        for (Long userId : userIds) {
            for (Long roleId : roleIds) {
                this.deleteRole(userId, roleId);
            }
        }
    }
}
