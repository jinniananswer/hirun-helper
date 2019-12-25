package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.IEmployeeTransDetailService;
import com.microtomato.hirun.modules.organization.service.IHrPendingDomainService;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@RestController
@Slf4j
@RequestMapping("api/organization/hr-pending")
public class HrPendingController {


    @Autowired
    private IHrPendingDomainService hrPendingDomainService;

    @Autowired
    private IHrPendingService hrPendingService;

    @Autowired
    private IEmployeeTransDetailService transDetailService;

    @GetMapping("/queryTransPendingByEmployeeId")
    @RestResult
    public IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Integer page, Integer limit) {
        Page<HrPending> hrPendingPage = new Page<HrPending>(page, limit);
        IPage<HrPendingInfoDTO> iPage = hrPendingDomainService.queryTransPendingByEmployeeId(employeeId, hrPendingPage);
        return iPage;
    }

    /**
     * 新增待办记录·
     */
    @PostMapping("/addHrPending")
    @RestResult
    public boolean addHrPending(HrPending hrPending) {
        Boolean result = hrPendingDomainService.addHrPending(hrPending);
        return result;
    }

    /**
     * 修改待办记录
     */
    @PostMapping("/updateHrPending")
    @RestResult
    public boolean updateHrPending(HrPending hrPending) {
        return hrPendingDomainService.updateHrPending(hrPending);
    }

    /**
     * 删除待办
     */
    @PostMapping("/deleteHrPending")
    @RestResult
    public boolean deleteHrPending(HrPending hrPending) {
        return hrPendingDomainService.deleteHrPending(hrPending);
    }

    /**
     * 根据执行人ID查询待办
     */
    @GetMapping("/queryPendingByExecuteId")
    @RestResult
    public IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPending, Integer page, Integer limit) {
        Page<HrPending> hrPendingPage = new Page<HrPending>(page, limit);
        return hrPendingDomainService.queryPendingByExecuteId(hrPending, hrPendingPage);
    }

    /**
     * 确认员工调动待办
     */
    @PostMapping("/confirmTransPending")
    @RestResult
    public boolean confirmTransPending(EmployeeTransDetailDTO employeeTransInfo) {
        transDetailService.confirmTransPending(employeeTransInfo);
        return true;
    }

    /**
     * 查询待办处理详情
     */
    @GetMapping("/getDetail")
    @RestResult
    public EmployeeTransDetailDTO getDetail(Long id) {
        EmployeeTransDetailDTO hrPendingDetailDTO = hrPendingDomainService.queryPendingDetailById(id);
        return hrPendingDetailDTO;
    }

    /**
     * 统计待办数据
     */
    @PostMapping("/countPending")
    @RestResult
    public List<EmployeePieStatisticDTO> countPending(Long employeeId) {
        if(employeeId==null){
            UserContext userContext = WebContextUtils.getUserContext();
            employeeId=userContext.getEmployeeId();
        }
        List<EmployeePieStatisticDTO> list = hrPendingService.countPending(employeeId);
        return list;
    }

}
