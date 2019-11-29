package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import com.microtomato.hirun.modules.organization.mapper.OrgHrRelMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-27
 */
@Slf4j
@Service
public class OrgHrRelServiceImpl extends ServiceImpl<OrgHrRelMapper, OrgHrRel> implements IOrgHrRelService {

    @Autowired
    private OrgHrRelMapper mapper;

    @Autowired
    private IEmployeeService employeeService;

    @Override
    public OrgHrRel queryValidQrgHrRel(Long orgId) {
        QueryWrapper<OrgHrRel> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("org_id",orgId);
        queryWrapper.apply("now() between start_time and end_time");
        List<OrgHrRel> hrRelList=this.mapper.selectList(queryWrapper);
        if(hrRelList.size()>0){
            return hrRelList.get(0);
        }

        return null;
    }

    @Override
    public Employee queryValidRemindEmployeeId(String employeeType, Long orgId) {
        Employee employee=null;
        QueryWrapper<OrgHrRel> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("org_id",orgId);
        queryWrapper.apply("now() between start_time and end_time");
        List<OrgHrRel> hrRelList=this.mapper.selectList(queryWrapper);
        if(hrRelList.size()<=0){
            return null;
        }
        OrgHrRel orgHrRel=hrRelList.get(0);

        if(StringUtils.equals(employeeType,"archive_manager")){
            if(orgHrRel.getArchiveManagerEmployeeId()==null){
                return null;
            }
            //根据配置查询员工的有效信息
             employee = employeeService.getOne(new QueryWrapper<Employee>().lambda()
                    .eq(Employee::getEmployeeId, orgHrRel.getArchiveManagerEmployeeId())
                    .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));

            return employee;
        }else{
            if(orgHrRel.getRelationManagerEmployeeId()==null){
                return null;
            }
            //根据配置查询员工的有效信息
            employee = employeeService.getOne(new QueryWrapper<Employee>().lambda()
                    .eq(Employee::getEmployeeId, orgHrRel.getRelationManagerEmployeeId())
                    .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));

            return employee;
        }

    }
}