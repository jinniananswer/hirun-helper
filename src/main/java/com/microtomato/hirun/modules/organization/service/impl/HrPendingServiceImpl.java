package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.consts.HrPendingConst;
import com.microtomato.hirun.modules.organization.entity.dto.HrPendingInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.mapper.HrPendingMapper;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@Slf4j
@Service
public class HrPendingServiceImpl extends ServiceImpl<HrPendingMapper, HrPending> implements IHrPendingService {

    @Autowired
    HrPendingMapper hrPendingMapper;

    @Override
    public IPage<HrPendingInfoDTO> queryTransPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage) {
        QueryWrapper<HrPending> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", employeeId);
        queryWrapper.in("pending_type", Arrays.asList(1, 2));
        queryWrapper.orderByDesc("create_time");
        queryWrapper.apply("end_time > start_time");

        return this.hrPendingMapper.queryTransPendingByEmployeeId(pendingPage, queryWrapper);
    }

    @Override
    public IPage<HrPendingInfoDTO> queryPendingByExecuteId(HrPending hrPending, Page<HrPending> pendingPage) {
        QueryWrapper<HrPending> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(hrPending.getPendingStatus()), "a.pending_status", hrPending.getPendingStatus());
        queryWrapper.eq(StringUtils.isNotBlank(hrPending.getPendingType()), "a.pending_type", hrPending.getPendingType());
        queryWrapper.eq("a.pending_execute_id", hrPending.getPendingExecuteId());
        queryWrapper.orderByDesc("a.create_time");
        return this.hrPendingMapper.queryPendingByExecuteId(pendingPage, queryWrapper);
    }

    @Override
    public List<HrPending> queryVaildPendingByEmployeeId(Long employeeId) {
        QueryWrapper<HrPending> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id",employeeId);
        queryWrapper.in("pending_type", Arrays.asList(1, 2));
        queryWrapper.eq("pending_status", HrPendingConst.PENDING_STATUS_1);
        return this.hrPendingMapper.selectList(queryWrapper);
    }


}
