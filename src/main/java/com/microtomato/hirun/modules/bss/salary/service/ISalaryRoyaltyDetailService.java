package com.microtomato.hirun.modules.bss.salary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.*;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;

import java.util.List;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
public interface ISalaryRoyaltyDetailService extends IService<SalaryRoyaltyDetail> {

    List<SalaryRoyaltyDetail> queryByOrderIdEmployeeIdItems(Long orderId, Long employeeId, List<String> items);

    List<SalaryRoyaltyDetail> queryByOrderIds(List<Long> orderIds);

    SalaryRoyaltyDetailDTO queryByOrderId(Long orderId);

    void saveDesignRoyaltyDetails(List<DesignRoyaltyDetailDTO> designRoyaltyDetails);

    void auditDesignRoyaltyDetails(List<DesignRoyaltyDetailDTO> designRoyaltyDetails);

    void saveProjectRoyaltyDetails(List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails);

    void auditProjectRoyaltyDetails(List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails);

    DesignRoyaltyDetailDTO afterCreateDesignDetail(DesignRoyaltyDetailDTO detail);

    ProjectRoyaltyDetailDTO afterCreateProjectDetail(ProjectRoyaltyDetailDTO detail);

    IPage<DesignRoyaltyDetailDTO> queryAuditDesignRoyaltyDetails(QueryRoyaltyDetailDTO request);

    IPage<ProjectRoyaltyDetailDTO> queryAuditProjectRoyaltyDetails(QueryRoyaltyDetailDTO request);

    void auditDesignRoyaltyPass(List<DesignRoyaltyDetailDTO> designDetails);

    void auditDesignRoyaltyNo(List<DesignRoyaltyDetailDTO> designDetails);

    void auditProjectRoyaltyPass(List<ProjectRoyaltyDetailDTO> projectDetails);

    void auditProjectRoyaltyNo(List<ProjectRoyaltyDetailDTO> projectDetails);

    IPage<StatRoyaltyDetailDTO> statByCustOrder(QueryStatRoyaltyDTO condition);
}