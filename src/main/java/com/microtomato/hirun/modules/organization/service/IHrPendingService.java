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
    IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage);

    IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPendingInfoDTO, Page<HrPending> pendingPage);

    List<HrPending> queryVaildPendingByEmployeeId(Long employeeId);
}
