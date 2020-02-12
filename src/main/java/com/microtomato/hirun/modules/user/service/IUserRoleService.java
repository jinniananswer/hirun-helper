package com.microtomato.hirun.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserRole;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 根据 User 查 UserRole
     *
     * @param user
     * @return
     */
    List<UserRole> queryUserRole(User user);

    /**
     * 立即生效，结束时间永久。
     *
     * @param userId  用户Id
     * @param orgId   部门Id，对应 ins_org.org_id
     * @param jobRole 岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     */
    void createRole(Long userId, Long orgId, String jobRole, String orgNature);

    /**
     * 根据给定的 用户Id、部门Id、以及岗位，创建对应的 ins_user_role 关联数据。
     *
     * 由于存在分公司的情况，每个分公司可能有相同的岗位，为了简化配置，支持 orgId 配置成 0 的情况。
     *
     * @param userId    用户Id
     * @param orgId     部门Id，对应 ins_org.org_id
     * @param jobRole   岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     * @param orgNature 部门行政
     * @param startDate 生效时间
     * @param endDate   失效时间
     */
    void createRole(Long userId, Long orgId, String jobRole, String orgNature, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 角色变更，立即终止所有老的角色，并重新创建一个新的角色
     *
     * @param userId 用户Id
     * @param orgId  原部门Id
     * @param jobRole 岗位
     * @param orgNature 部门性质
     */
    void switchRole(Long userId, Long orgId, String jobRole, String orgNature);

    /**
     * 根据 userId, roleId 立即终止其对应的角色
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    void deleteRole(Long userId, Long roleId);

    /**
     * 根据 userId, roleId 在指定的时间终止其对应的角色
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     * @param endDatetime 终止时间
     */
    void deleteRole(Long userId, Long roleId, LocalDateTime endDatetime);

    /**
     * 根据 userId 立即终止其对应的所有角色
     *
     * @param userId 用户Id
     */
    void deleteAllRole(Long userId);

    /**
     * 根据 userId 在指定的时间，终止其对应的所有角色
     *
     * @param userId 用户Id
     * @param endDatetime 终止时间
     */
    void deleteAllRole(Long userId, LocalDateTime endDatetime);

    /**
     * 批量分配角色
     *
     * @param userIds
     * @param roleIds
     */
    void grantRole(List<Long> userIds, List<Long> roleIds);

    /**
     * 批量回收角色
     *
     * @param userIds
     * @param roleIds
     */
    void revokeRole(List<Long> userIds, List<Long> roleIds);
}
