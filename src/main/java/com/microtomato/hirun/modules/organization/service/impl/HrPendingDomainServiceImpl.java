package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.BaseException;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.HrPendingConst;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.domain.HrPendingDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.*;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuhui
 * @program: hirun-helper
 * @description: HR待办领域服务实现类
 **/
@Slf4j
@Service
public class HrPendingDomainServiceImpl implements IHrPendingDomainService {

    @Autowired
    private IHrPendingService hrPendingService;
    @Autowired
    private HrPendingDO hrPendingDO;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeTransDetailService detailService;


    @Override
    @DS("ins")
    public boolean addHrPending(HrPending hrPending) {
        //判断该员工是否还存在其他申请转部门记录，如果又则不让新增申请
        List<HrPending> pendingList=hrPendingService.queryVaildPendingByEmployeeId(hrPending.getEmployeeId());
        if(ArrayUtils.isNotEmpty(pendingList)){
            throw new BaseException("该员工存在未处理的调动待办任务，请处理后再新增.",400001);
        }

        UserContext userContext = WebContextUtils.getUserContext();
        hrPending.setPendingCreateId(userContext.getEmployeeId());
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_1);
        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_2)) {
            hrPending.setEndTime(TimeUtils.stringToLocalDateTime("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }
        int result = hrPendingDO.add(hrPending);
        //todo 发送消息
        if (result <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage) {
        IPage<HrPendingInfoDTO> iPage = hrPendingService.queryTransPendingByEmployeeId(employeeId, pendingPage);
        if (iPage.getTotal() <= 0) {
            return iPage;
        }
        //翻译名字
        List<HrPendingInfoDTO> dtoList = new ArrayList<>();
        for (HrPendingInfoDTO hrPendingInfoDTO : iPage.getRecords()) {
            hrPendingInfoDTO.setEmployeeName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getEmployeeId()));
            hrPendingInfoDTO.setPendingCreateName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingCreateId()));
            hrPendingInfoDTO.setPendingExecuteName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingExecuteId()));
            dtoList.add(hrPendingInfoDTO);
        }
        return iPage.setRecords(dtoList);
    }

    /**
     * 删除待办
     *
     * @param hrPending
     * @return
     */
    @Override
    public boolean deleteHrPending(HrPending hrPending) {
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_3);
        //todo 发送消息
        return hrPendingDO.delete(hrPending);
    }

    @Override
    public boolean updateHrPending(HrPending hrPending) {
        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_2)) {
            hrPending.setEndTime(TimeUtils.stringToLocalDateTime("2099-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }
        return hrPendingDO.update(hrPending);
    }

    @Override
    public IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPending, Page<HrPending> pendingPage) {
        UserContext userContext = WebContextUtils.getUserContext();
        hrPending.setPendingExecuteId(userContext.getEmployeeId());
        IPage<HrPendingInfoDTO> iPage = hrPendingService.queryPendingByExecuteId(hrPending, pendingPage);
        if (iPage.getTotal() <= 0) {
            return iPage;
        }
        //翻译名字
        List<HrPendingInfoDTO> dtoList = new ArrayList<>();
        for (HrPendingInfoDTO hrPendingInfoDTO : iPage.getRecords()) {
            hrPendingInfoDTO.setEmployeeName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getEmployeeId()));
            hrPendingInfoDTO.setPendingCreateName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingCreateId()));
            hrPendingInfoDTO.setPendingExecuteName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingExecuteId()));
            dtoList.add(hrPendingInfoDTO);
        }
        return iPage.setRecords(dtoList);
    }

    /**
     * 确定员工调动待办
     *
     * @param transDetail
     * @return
     */
    @DS("ins")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public boolean confirmTransPending(EmployeeTransDetailDTO transDetail) {
        //todo  还应该加上该员工如果为调出的情况下，存在下级员工需要转移下级员工
        Employee employee = new Employee();
        EmployeeJobRole employeeJobRole = new EmployeeJobRole();
        EmployeeTransDetail employeeTransDetail = new EmployeeTransDetail();
        HrPending hrPending = new HrPending();
        //根据传入dto复制属性值
        BeanUtils.copyProperties(transDetail, employeeJobRole);
        BeanUtils.copyProperties(transDetail, employee);
        BeanUtils.copyProperties(transDetail, employeeTransDetail);
        BeanUtils.copyProperties(transDetail, hrPending);

        //查询未更新前的员工居住地址信息，为了拼装完整的调动记录
        Employee originalEmployee = employeeService.getById(transDetail.getEmployeeId());
        employeeTransDetail.setSourceHomeAddress(originalEmployee.getHomeAddress());
        employeeTransDetail.setSourceHomeProv(originalEmployee.getHomeProv());
        employeeTransDetail.setSourceHomeCity(originalEmployee.getHomeCity());
        employeeTransDetail.setSourceHomeRegion(originalEmployee.getHomeRegion());
        //修改员工现居住地址
        EmployeeDO employeeDo=SpringContextUtils.getBean(EmployeeDO.class,employee);
        employeeDo.modify(employee,null,null, null);
        //查询出未更新前的员工jobrole数据，拼装完整调动记录
        EmployeeJobRole validEmployeeJobRole = employeeJobRoleService.queryValidMain(transDetail.getEmployeeId());

        employeeTransDetail.setSourceDiscountRate(validEmployeeJobRole.getDiscountRate());
        employeeTransDetail.setSourceJobRoleNature(validEmployeeJobRole.getJobRoleNature());
        employeeTransDetail.setSourceParentEmployeeId(validEmployeeJobRole.getParentEmployeeId());
        employeeTransDetail.setSourceJobRole(validEmployeeJobRole.getJobRole());
        employeeTransDetail.setSourceOrgId(validEmployeeJobRole.getOrgId());
        //结束原来生效的jobrole数据
        employeeDo.destroy(transDetail.getStartTime());
        //新增员工jobRole数据
        employeeJobRole.setIsMain(validEmployeeJobRole.getIsMain());
        employeeJobRole.setRemark(validEmployeeJobRole.getRemark());
        employeeJobRole.setStartDate(transDetail.getStartTime());
        if (StringUtils.equals(transDetail.getPendingType(), HrPendingConst.PENDING_TYPE_2)) {
            employeeJobRole.setEndDate(TimeUtils.stringToLocalDateTime("2999-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        } else {
            employeeJobRole.setEndDate(transDetail.getEndTime());
        }
        //todo 如果这个地方选择的为借调需要再插一条根据结束时间到2099年的数据。前提先找洪慧确认借调走不走这套流程
        employeeJobRoleService.save(employeeJobRole);
        //拼装调动记录的其他数据
        employeeTransDetail.setRelPendingId(transDetail.getId());
        detailService.save(employeeTransDetail);

        //更新待办数据
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_2);
        boolean result = hrPendingService.updateById(hrPending);
        //todo 发送消息重新签订合同与回执消息
        return result;
    }

    @Override
    public EmployeeTransDetailDTO queryPendingDetailById(Long id) {
        EmployeeTransDetail employeeTransDetail = detailService.queryPendingDetailById(id);
        if (employeeTransDetail == null) {
            return null;
        }
        EmployeeTransDetailDTO hrPendingDetailDTO = new EmployeeTransDetailDTO();
        BeanUtils.copyProperties(employeeTransDetail, hrPendingDetailDTO);

        hrPendingDetailDTO.setSourceParentEmployeeName(employeeService.getEmployeeNameEmployeeId(hrPendingDetailDTO.getSourceParentEmployeeId()));
        hrPendingDetailDTO.setParentEmployeeName(employeeService.getEmployeeNameEmployeeId(hrPendingDetailDTO.getParentEmployeeId()));
        OrgDO sourceOrgDO=SpringContextUtils.getBean(OrgDO.class,employeeTransDetail.getSourceOrgId());
        hrPendingDetailDTO.setSourceOrgPath(sourceOrgDO.getCompanyLinePath());
        OrgDO targetOrgDO=SpringContextUtils.getBean(OrgDO.class,employeeTransDetail.getOrgId());
        hrPendingDetailDTO.setOrgPath(targetOrgDO.getCompanyLinePath());
        hrPendingDetailDTO.setSourceJobRoleName(staticDataService.getCodeName("JOB_ROLE", hrPendingDetailDTO.getSourceJobRole()));
        hrPendingDetailDTO.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", hrPendingDetailDTO.getJobRole()));
        hrPendingDetailDTO.setSourceJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE",hrPendingDetailDTO.getSourceJobRoleNature()));
        hrPendingDetailDTO.setJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE",hrPendingDetailDTO.getJobRoleNature()));
        return hrPendingDetailDTO;
    }

}
