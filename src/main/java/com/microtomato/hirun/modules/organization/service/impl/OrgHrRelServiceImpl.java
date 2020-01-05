package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.OrgHrRelInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeOrgRel;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import com.microtomato.hirun.modules.organization.mapper.OrgHrRelMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeOrgRelService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
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

    @Autowired
    private IOrgService orgService;

    @Autowired
    IEmployeeOrgRelService employeeOrgRelService;

    @Override
    public OrgHrRel queryValidQrgHrRel(Long orgId) {
        QueryWrapper<OrgHrRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_id", orgId);
        queryWrapper.apply("now() between start_time and end_time");
        List<OrgHrRel> hrRelList = this.mapper.selectList(queryWrapper);
        if (hrRelList.size() > 0) {
            return hrRelList.get(0);
        }
        return null;
    }

    @Override
    public Employee queryValidRemindEmployeeId(String employeeType, Long orgId) {
        Employee employee = null;
        QueryWrapper<OrgHrRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_id", orgId);
        queryWrapper.apply("now() between start_time and end_time");
        List<OrgHrRel> hrRelList = this.mapper.selectList(queryWrapper);
        if (hrRelList.size() <= 0) {
            return null;
        }
        OrgHrRel orgHrRel = hrRelList.get(0);

        if (StringUtils.equals(employeeType, "archive_manager")) {
            if (orgHrRel.getArchiveManagerEmployeeId() == null) {
                return null;
            }
            //根据配置查询员工的有效信息
            employee = employeeService.getOne(new QueryWrapper<Employee>().lambda()
                    .eq(Employee::getEmployeeId, orgHrRel.getArchiveManagerEmployeeId())
                    .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));

            return employee;
        } else {
            if (orgHrRel.getRelationManagerEmployeeId() == null) {
                return null;
            }
            //根据配置查询员工的有效信息
            employee = employeeService.getOne(new QueryWrapper<Employee>().lambda()
                    .eq(Employee::getEmployeeId, orgHrRel.getRelationManagerEmployeeId())
                    .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));

            return employee;
        }

    }

    @Override
    public IPage<OrgHrRelInfoDTO> queryOrgHrRelList(Long employeeId, Long orgSet, Page<OrgHrRel> page) {
        String orgLine="";
        if (orgSet==null) {
            orgLine = orgService.listOrgSecurityLine();
        }else{
            OrgDO orgSetDO = SpringContextUtils.getBean(OrgDO.class, orgSet);
            orgLine=orgSetDO.getOrgLine(orgSet);
        }

        QueryWrapper<OrgHrRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(employeeId != null, "(archive_manager_employee_id=" + employeeId + " or relation_manager_employee_id=" + employeeId + ")");
        queryWrapper.apply(StringUtils.isNotEmpty(orgLine), "a.org_id in (" + orgLine + ")");
        //queryWrapper.orderByAsc("a.org_id");

        IPage<OrgHrRelInfoDTO> iPage = this.mapper.queryOrgHrRelPage(page, queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }
        for (OrgHrRelInfoDTO infoDTO : iPage.getRecords()) {
            infoDTO.setArchiveManagerEmployeeName(employeeService.getEmployeeNameEmployeeId(infoDTO.getArchiveManagerEmployeeId()));
            infoDTO.setRelationManagerEmployeeName(employeeService.getEmployeeNameEmployeeId(infoDTO.getRelationManagerEmployeeId()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, infoDTO.getOrgId());
            infoDTO.setOrgPath(orgDO.getCompanyLinePath());
        }
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean updateOrgHrRel(String id, Long archEmployeeID, Long relationEmployeeId) {
        UserContext userContext = WebContextUtils.getUserContext();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.apply("org_id in (" + id + ")");
        OrgHrRel orgHrRel = new OrgHrRel();
        orgHrRel.setArchiveManagerEmployeeId(archEmployeeID);
        orgHrRel.setRelationManagerEmployeeId(relationEmployeeId);
        orgHrRel.setUpdateUserId(userContext.getUserId());
        int result = this.mapper.update(orgHrRel, updateWrapper);

        //同步数据给员工部门关系表
        List<EmployeeOrgRel> addList=new ArrayList<>();

        if(archEmployeeID.equals(relationEmployeeId)){
            employeeOrgRelService.updateEmployeeOrgRel(id,OrgConst.EMPLOYEE_REL_TYPE_RELEVANCE);
            String[] orgId=id.split(",");
            this.buildList(orgId,relationEmployeeId,addList,userContext);

        }else{
            employeeOrgRelService.updateEmployeeOrgRel(id,OrgConst.EMPLOYEE_REL_TYPE_RELEVANCE);
            employeeOrgRelService.updateEmployeeOrgRel(id,OrgConst.EMPLOYEE_REL_TYPE_RELEVANCE);

            String[] orgId=id.split(",");
            this.buildList(orgId,relationEmployeeId,addList,userContext);
            this.buildList(orgId,archEmployeeID,addList,userContext);
        }

        if(addList.size()>0){
            employeeOrgRelService.saveBatch(addList);
        }

        if (result <= 0) {
            return false;
        }
        return true;
    }

    private void buildList(String[] orgIds,Long employeeId,List addList,UserContext userContext){
        if(orgIds.length<=0){
            return;
        }
        for(int i=0;i<orgIds.length;i++){
            EmployeeOrgRel employeeOrgRel=new EmployeeOrgRel();
            employeeOrgRel.setRelType(OrgConst.EMPLOYEE_REL_TYPE_RELEVANCE);
            employeeOrgRel.setEmployeeId(employeeId);
            employeeOrgRel.setOrgId(Long.parseLong(orgIds[i]));
            employeeOrgRel.setCreateTime(LocalDateTime.now());
            employeeOrgRel.setCreateUserId(userContext.getUserId());
            employeeOrgRel.setUpdateTime(LocalDateTime.now());
            employeeOrgRel.setUpdateUserId(userContext.getUserId());
            addList.add(employeeOrgRel);
        }
    }

    @Override
    public String getOrgLine(Long employeeId) {
        if (employeeId == null) {
            return "";
        }
        QueryWrapper<OrgHrRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("now() between start_time and end_time");
        queryWrapper.apply(employeeId != null, "(archive_manager_employee_id=" + employeeId + " or relation_manager_employee_id=" + employeeId + ")");
        List<OrgHrRel> list = this.list(queryWrapper);
        if (list.size() <= 0) {
            return null;
        }
        String orgLine = "";
        for (OrgHrRel orgHrRel : list) {
            orgLine += orgHrRel.getOrgId() + ",";
        }
        return orgLine.substring(0,orgLine.length()-1);
    }

    @Override
    public List<OrgHrRel> queryOrgHrRelByEmployeeId(Long employeeId) {
        QueryWrapper<OrgHrRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("now() between start_time and end_time");
        queryWrapper.apply(employeeId != null, "(archive_manager_employee_id=" + employeeId + " or relation_manager_employee_id=" + employeeId + ")");
        return this.list(queryWrapper);
    }
}
