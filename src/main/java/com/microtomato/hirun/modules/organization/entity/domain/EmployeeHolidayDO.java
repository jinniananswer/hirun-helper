package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.microtomato.hirun.modules.organization.mapper.EmployeeBlacklistMapper;
import com.microtomato.hirun.modules.organization.mapper.EmployeeHolidayMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 员工休假领域对象
 * @author: liuhui
 **/
@Slf4j
@Component
@Scope("prototype")
public class EmployeeHolidayDO {

    @Autowired
    private EmployeeHolidayMapper employeeHolidayMapper;


    /**
     * 新增员工休假
     */
    public int newEntry(EmployeeHoliday employeeHoliday) {
      //todo 判断假期是否重复
      return this.employeeHolidayMapper.insert(employeeHoliday);
    }

    /**
     * 判断假期是否重复
     */
    public boolean isIntersectHoliday(EmployeeHoliday employeeHoliday){
        return true;
    }

    /**
     * 更新员工休假信息
     */
    public int updateEntry(EmployeeHoliday employeeHoliday) {
        //todo 判断假期是否重复
        int updateResult=employeeHolidayMapper.updateById(employeeHoliday);
        return updateResult;
    }

    /**
     * 删除员工休假信息
     */
    public int deleteEntry(EmployeeHoliday employeeHoliday) {
        int deleteResult=employeeHolidayMapper.updateById(employeeHoliday);
        return deleteResult;
    }
}
