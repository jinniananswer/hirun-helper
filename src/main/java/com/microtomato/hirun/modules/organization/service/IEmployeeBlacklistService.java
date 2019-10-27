package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-14
 */
public interface IEmployeeBlacklistService extends IService<EmployeeBlacklist> {

    boolean exists(String identityNo);
}
