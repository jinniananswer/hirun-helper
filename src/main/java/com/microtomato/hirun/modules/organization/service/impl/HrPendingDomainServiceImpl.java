package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.HrPendingConst;
import com.microtomato.hirun.modules.organization.entity.domain.HrPendingDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.*;
import com.microtomato.hirun.modules.system.entity.domain.AddressDO;
import com.microtomato.hirun.modules.system.service.INotifyService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private INotifyService notifyService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeTransDetailService detailService;

    @Autowired
    private IEmployeeBlacklistService blacklistService;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean addHrPending(HrPending hrPending) {
        //判断该员工是否还存在其他申请转部门记录，如果有则不让新增申请
        List<HrPending> pendingList = hrPendingService.queryVaildPendingByEmployeeId(hrPending.getEmployeeId());
        if (ArrayUtils.isNotEmpty(pendingList)) {
            throw new AlreadyExistException(" 该员工存在未处理的调动待办任务，请处理后再新增.", ErrorKind.ALREADY_EXIST.getCode());
        }
        //判断员工是否有在生效的借调记录
        List<HrPending> effectList = hrPendingService.queryEffectBorrowPendingByEmployeeId(hrPending.getEmployeeId());
        if (effectList.size() > 0) {
            throw new AlreadyExistException(" 该员工存在正在生效的借调记录，请处理后再新增.", ErrorKind.ALREADY_EXIST.getCode());
        }
        //判断进行调动申请的员工是否有下属员工
        List<EmployeeInfoDTO> childEmployeeList = employeeService.findSubordinate(hrPending.getEmployeeId());
        if (childEmployeeList.size() > 0) {
            throw new AlreadyExistException("该员工下存在下属员工，请将下属员工转移之后，再进行调动申请。", ErrorKind.ALREADY_EXIST.getCode());
        }


        UserContext userContext = WebContextUtils.getUserContext();
        hrPending.setPendingCreateId(userContext.getEmployeeId());
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_1);
        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_TRANS)) {
            hrPending.setEndTime(TimeUtils.getForeverTime());
        }
        String content = employeeService.getEmployeeNameEmployeeId(hrPending.getPendingExecuteId()) + ",你好。"
                + employeeService.getEmployeeNameEmployeeId(userContext.getEmployeeId()) + "发起了员工【"
                + employeeService.getEmployeeNameEmployeeId(hrPending.getEmployeeId()) + "】的调动申请。调动时间为"
                + hrPending.getStartTime() + "至" + hrPending.getEndTime()
                + "。请到期进行待办确认！";

        hrPending.setContent(content);
        int result = hrPendingDO.add(hrPending);
        //发送消息
        notifyService.sendMessage(hrPending.getPendingExecuteId(),content,userContext.getEmployeeId());
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
        for (HrPendingInfoDTO hrPendingInfoDTO : iPage.getRecords()) {
            hrPendingInfoDTO.setEmployeeName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getEmployeeId()));
            hrPendingInfoDTO.setPendingCreateName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingCreateId()));
            hrPendingInfoDTO.setPendingExecuteName(employeeService.getEmployeeNameEmployeeId(hrPendingInfoDTO.getPendingExecuteId()));
        }
        return iPage;
    }

    /**
     * 删除待办
     *
     * @param hrPending
     * @return
     */
    @Override
    public boolean deleteHrPending(HrPending hrPending) {
        UserContext userContext = WebContextUtils.getUserContext();
        HrPending originalHrPending = hrPendingService.getById(hrPending.getId());

        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_3);

        boolean result = hrPendingDO.delete(hrPending);

        //发送消息告知人资调动申请已删除

        String content = employeeService.getEmployeeNameEmployeeId(originalHrPending.getPendingExecuteId()) + ",你好。"
                + employeeService.getEmployeeNameEmployeeId(originalHrPending.getPendingCreateId()) + "发起的员工【"
                + employeeService.getEmployeeNameEmployeeId(originalHrPending.getEmployeeId()) + "】的调动申请，已删除，请知悉。删除时间为"
                + LocalDateTime.now();

        notifyService.sendMessage(originalHrPending.getPendingExecuteId(),content,originalHrPending.getPendingCreateId());
        return result;
    }

    @Override
    public boolean updateHrPending(HrPending hrPending) {
        UserContext userContext = WebContextUtils.getUserContext();

        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_TRANS)) {
            hrPending.setEndTime(TimeUtils.getForeverTime());
        }

        boolean result = hrPendingDO.update(hrPending);

        String content = employeeService.getEmployeeNameEmployeeId(hrPending.getPendingExecuteId()) + ",你好。"
                + employeeService.getEmployeeNameEmployeeId(userContext.getEmployeeId()) + "发起了员工【"
                + employeeService.getEmployeeNameEmployeeId(hrPending.getEmployeeId()) + "】的调动申请修改。调动时间为"
                + hrPending.getStartTime() + "至" + hrPending.getEndTime()
                + "。请到期进行待办确认！";

        notifyService.sendMessage(hrPending.getPendingExecuteId(),content,userContext.getEmployeeId());
        return result;
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

        OrgDO sourceOrgDO = SpringContextUtils.getBean(OrgDO.class, employeeTransDetail.getSourceOrgId());
        hrPendingDetailDTO.setSourceOrgPath(sourceOrgDO.getCompanyLinePath());

        OrgDO targetOrgDO = SpringContextUtils.getBean(OrgDO.class, employeeTransDetail.getOrgId());
        hrPendingDetailDTO.setOrgPath(targetOrgDO.getCompanyLinePath());

        AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
        log.debug(addressDO.getFullName(employeeTransDetail.getHomeRegion()));
        hrPendingDetailDTO.setHomeArea(addressDO.getFullName(employeeTransDetail.getHomeRegion()));
        hrPendingDetailDTO.setSourceHomeArea(addressDO.getFullName(employeeTransDetail.getSourceHomeRegion()));

        hrPendingDetailDTO.setSourceJobRoleName(staticDataService.getCodeName("JOB_ROLE", hrPendingDetailDTO.getSourceJobRole()));
        hrPendingDetailDTO.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", hrPendingDetailDTO.getJobRole()));
        hrPendingDetailDTO.setSourceJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE", hrPendingDetailDTO.getSourceJobRoleNature()));
        hrPendingDetailDTO.setJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE", hrPendingDetailDTO.getJobRoleNature()));

        hrPendingDetailDTO.setSourceJobGradeName(staticDataService.getCodeName("JOB_GRADE",hrPendingDetailDTO.getSourceJobGrade()));
        hrPendingDetailDTO.setJobGradeName(staticDataService.getCodeName("JOB_GRADE",hrPendingDetailDTO.getJobGrade()));

        return hrPendingDetailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeBlackListApply(Long employeeId, String remark) {
        UserContext userContext=WebContextUtils.getUserContext();
        HrPending hrPending=new HrPending();
        hrPending.setEmployeeId(employeeId);
        hrPending.setPendingCreateId(userContext.getEmployeeId());
        hrPending.setPendingExecuteId(107L);
        hrPending.setRemark(remark);
        hrPending.setPendingType(HrPendingConst.PENDING_TYPE_EMPLOYEEBLACK);
        hrPending.setStartTime(LocalDateTime.now());
        hrPending.setEndTime(TimeUtils.getForeverTime());
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_1);
        String content=employeeService.getEmployeeNameEmployeeId(107L) + ",您好。"
                + employeeService.getEmployeeNameEmployeeId(userContext.getEmployeeId()) + "发起了员工【"
                + employeeService.getEmployeeNameEmployeeId(employeeId) + "】的加入黑名单申请，请进行待办处理！";
        hrPending.setContent(content);

        hrPendingService.save(hrPending);
        //发送消息
        notifyService.sendMessage(hrPending.getPendingExecuteId(),content,userContext.getEmployeeId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void approveEmployeeBlackListPending(Long employeeId, Long id, String approveStatus) {
        UserContext userContext=WebContextUtils.getUserContext();
        HrPending hrPending=this.hrPendingService.getById(id);
        Employee employee=employeeService.getById(employeeId);

        if(StringUtils.equals(approveStatus,"2")){
            String content=employeeService.getEmployeeNameEmployeeId(hrPending.getPendingCreateId())+",您好！【" +
                    employeeService.getEmployeeNameEmployeeId(userContext.getEmployeeId())+"】未审核通过员工【" +
                    employeeService.getEmployeeNameEmployeeId(hrPending.getEmployeeId())+"】的加入黑名单请求！";
            notifyService.sendMessage(hrPending.getPendingCreateId(),content,userContext.getEmployeeId());
        }else{
            blacklistService.addEmployeeBlackList(employee,hrPending.getRemark()+"。人资发起，集团审核通过加入黑名单。");
            String content=employeeService.getEmployeeNameEmployeeId(hrPending.getPendingCreateId())+",您好！【" +
                    employeeService.getEmployeeNameEmployeeId(userContext.getEmployeeId())+"】审核通过员工【" +
                    employeeService.getEmployeeNameEmployeeId(hrPending.getEmployeeId())+"】的加入黑名单请求！";
            notifyService.sendMessage(hrPending.getPendingCreateId(),content,userContext.getEmployeeId());
        }

        hrPending.setPendingStatus("2");
        this.hrPendingService.updateById(hrPending);
    }


}
