package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import com.microtomato.hirun.modules.organization.mapper.EmployeeBlacklistMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 员工领域对象
 * @author: liuhui
 * @create: 2019-10-14
 **/
@Slf4j
@Component
@Scope("prototype")
public class EmployeeBlackListDO {

    @Autowired
    private EmployeeBlacklistMapper employeeBlacklistMapper;


    /**
     * 新增黑名单记录
     */
    public void newEntry(EmployeeBlacklist employeeBlacklist) {
        this.employeeBlacklistMapper.insert(employeeBlacklist);
    }


}
