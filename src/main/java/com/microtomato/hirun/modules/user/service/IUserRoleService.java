package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 根据给定的 用户Id、部门Id、以及岗位，创建对应的 ins_user_role 关联数据。
     *
     * @param userId 用户Id
     * @param orgId 部门Id，对应 ins_org.org_id
     * @param jobRole 岗位编码，对应 code_value, select * from sys_static_data t where t.code_type='JOB_ROLE';
     */
    void createUserRole(Long userId, Long orgId, String jobRole);
}
