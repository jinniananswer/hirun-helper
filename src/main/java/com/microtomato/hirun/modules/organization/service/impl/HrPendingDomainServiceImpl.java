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
import com.microtomato.hirun.modules.system.entity.domain.AddressDO;
import com.microtomato.hirun.modules.system.entity.po.Notify;
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
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeTransDetailService detailService;


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
        List<Employee> childEmployeeList = employeeService.findSubordinate(hrPending.getEmployeeId());
        if (childEmployeeList.size() > 0) {
            throw new AlreadyExistException("该员工下存在下属员工，请将下属员工转移之后，再进行调动申请。", ErrorKind.ALREADY_EXIST.getCode());
        }


        UserContext userContext = WebContextUtils.getUserContext();
        hrPending.setPendingCreateId(userContext.getEmployeeId());
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_1);
        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_2)) {
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

        if (StringUtils.equals(hrPending.getPendingType(), HrPendingConst.PENDING_TYPE_2)) {
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

    /**
     * 确定员工调动待办
     *
     * @param transDetail
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public boolean confirmTransPending(EmployeeTransDetailDTO transDetail) {
        UserContext userContext = WebContextUtils.getUserContext();

        List<Employee> childEmployeeList = employeeService.findSubordinate(transDetail.getEmployeeId());
        if (childEmployeeList.size() > 0) {
            throw new AlreadyExistException("该员工下存在下属员工，请将下属员工转移之后，再进行调动申请。", ErrorKind.ALREADY_EXIST.getCode());
        }

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
        EmployeeDO employeeDo = SpringContextUtils.getBean(EmployeeDO.class, employee);
        employeeDo.modify(employee, null, null, null);
        //查询出未更新前的员工jobrole数据，拼装完整调动记录
        EmployeeJobRole validEmployeeJobRole = employeeJobRoleService.queryValidMain(transDetail.getEmployeeId());

        employeeTransDetail.setSourceDiscountRate(validEmployeeJobRole.getDiscountRate());
        employeeTransDetail.setSourceJobRoleNature(validEmployeeJobRole.getJobRoleNature());
        employeeTransDetail.setSourceParentEmployeeId(validEmployeeJobRole.getParentEmployeeId());
        employeeTransDetail.setSourceJobRole(validEmployeeJobRole.getJobRole());
        employeeTransDetail.setSourceOrgId(validEmployeeJobRole.getOrgId());
        //结束原来生效的jobrole数据
        employeeDo.destroyJob(transDetail.getStartTime());
        //新增员工jobRole数据
        employeeJobRole.setIsMain(validEmployeeJobRole.getIsMain());
        employeeJobRole.setRemark(validEmployeeJobRole.getRemark());
        employeeJobRole.setStartDate(transDetail.getStartTime());
        employeeJobRole.setEndDate(TimeUtils.getForeverTime());

        //借调同样走借出流程
        employeeJobRoleService.save(employeeJobRole);
        //拼装调动记录的其他数据
        employeeTransDetail.setRelPendingId(transDetail.getId());
        detailService.save(employeeTransDetail);

        //更新待办数据
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_2);
        boolean result = hrPendingService.updateById(hrPending);

        //调动状态为调出，则发送消息提醒重签合同
        if (StringUtils.equals(transDetail.getTransType(), HrPendingConst.PENDING_TYPE_2)) {
            String content = employeeService.getEmployeeNameEmployeeId(transDetail.getEmployeeId()) +
                    "，已确认调出,请签订变更协议！";
            notifyService.sendMessage(userContext.getEmployeeId(),content);
        }

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

        return hrPendingDetailDTO;
    }


}
