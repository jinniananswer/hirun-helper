package com.microtomato.hirun.modules.organization.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;


/**
 * @program: hirun-helper
 * @description: hr待办领域服务接口
 **/
public interface IHrPendingDomainService {

    /**
     *新增待办
     * @param hrPending
     * @return
     */
    boolean addHrPending(HrPending hrPending);

    /**
     *查询待办记录
     * @param
     * @return
     */
    IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage);

    /**
     * 删除待办
     */
    boolean deleteHrPending(HrPending hrPending);

    /**
     * 修改待办
     */
    boolean updateHrPending(HrPending hrPending);

    /**
     * 根据执行ID查询待办
     */
    IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPending, Page<HrPending> pendingPage);


    /**
     * 查询待办详情
     */
    EmployeeTransDetailDTO queryPendingDetailById(Long id);

    /**
     * 新增员工加入黑名单待办
     * @param employeeId
     * @param remark
     */
    void addEmployeeBlackListApply(Long employeeId,String remark);

    /**
     * 审核员工加入黑名单待办确认
     * @param employeeId
     * @param id
     * @param approveStatus
     */
    void approveEmployeeBlackListPending(Long employeeId,Long id,String approveStatus);
}
