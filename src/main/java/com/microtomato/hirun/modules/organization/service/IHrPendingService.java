package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
public interface IHrPendingService extends IService<HrPending> {
    /**
     *根据员工ID查询该员工申请的待办任务
     * @param employeeId
     * @param pendingPage
     * @return
     */
    IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage);

    /**
     * 根据待办处理人ID查询待办任务
     * @param hrPendingInfoDTO
     * @param pendingPage
     * @return
     */
    IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPendingInfoDTO, Page<HrPending> pendingPage);

    List<HrPending> queryVaildPendingByEmployeeId(Long employeeId);

    /**
     * 查询生效的借调记录
     * @param employeeId
     * @return
     */
    List<HrPending> queryEffectBorrowPendingByEmployeeId(Long employeeId);

    /**
     *根据待办执行人ID查询待办
     */
    List<HrPending> queryPendingByExecuteId(Long executId);
}
