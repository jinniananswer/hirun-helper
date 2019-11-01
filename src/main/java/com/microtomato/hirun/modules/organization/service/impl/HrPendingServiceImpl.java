package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.mapper.HrPendingMapper;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@Slf4j
@Service
public class HrPendingServiceImpl extends ServiceImpl<HrPendingMapper, HrPending> implements IHrPendingService {

    @Autowired HrPendingMapper hrPendingMapper;

    @Override
    public IPage<HrPending> queryPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage) {
        QueryWrapper<HrPending> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("employee_id",employeeId);
        queryWrapper.in("pending_type", Arrays.asList(1,2));

        return this.hrPendingMapper.selectPage(pendingPage,queryWrapper);
    }
}
