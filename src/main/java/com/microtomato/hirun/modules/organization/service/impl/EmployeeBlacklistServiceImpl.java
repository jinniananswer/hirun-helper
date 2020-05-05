package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import com.microtomato.hirun.modules.organization.mapper.EmployeeBlacklistMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public void addEmployeeBlackList(Employee employee, String remark) {
        EmployeeBlacklist employeeBlacklist=new EmployeeBlacklist();
        employeeBlacklist.setEmployeeId(employee.getEmployeeId());
        employeeBlacklist.setIdentityNo(employee.getIdentityNo());
        employeeBlacklist.setMobileNo(employee.getMobileNo());
        employeeBlacklist.setName(employee.getName());
        employeeBlacklist.setStartTime(LocalDateTime.now());
        employeeBlacklist.setEndTime(TimeUtils.getForeverTime());
        employeeBlacklist.setRemark(remark);
        this.baseMapper.insert(employeeBlacklist);
    }

    @Override
    public List<EmployeeBlacklist> queryEmployeeBlackList(String employeeName, String identityNo) {
        List<EmployeeBlacklist> employeeBlacklists=this.baseMapper.selectList(new QueryWrapper<EmployeeBlacklist>().lambda()
                .like(StringUtils.isNotEmpty(employeeName),EmployeeBlacklist::getName,employeeName)
                .like(StringUtils.isNotEmpty(identityNo),EmployeeBlacklist::getIdentityNo,identityNo)
                .apply(" now() between start_time and end_time "));
        return employeeBlacklists;
    }

    @Override
    public void deleteBlackList(Long id, String remark) {
        EmployeeBlacklist employeeBlack=this.baseMapper.selectById(id);
        if(employeeBlack!=null){
            employeeBlack.setEndTime(RequestTimeHolder.getRequestTime());
            employeeBlack.setRemark(employeeBlack.getRemark()+"::"+remark);
            this.baseMapper.updateById(employeeBlack);
        }
    }
}
