package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import com.microtomato.hirun.modules.organization.mapper.EmployeeBlacklistMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-14
 */
@Slf4j
@Service
public class EmployeeBlacklistServiceImpl extends ServiceImpl<EmployeeBlacklistMapper, EmployeeBlacklist> implements IEmployeeBlacklistService {

    @Override
    public boolean exists(String identityNo) {
        Integer count = this.count(new QueryWrapper<EmployeeBlacklist>().lambda().eq(EmployeeBlacklist::getIdentityNo, identityNo).apply("now() between start_time and end_time "));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
