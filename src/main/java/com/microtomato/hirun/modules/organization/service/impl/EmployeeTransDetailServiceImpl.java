package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.mapper.EmployeeTransDetailMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeTransDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-29
 */
@Slf4j
@Service
public class EmployeeTransDetailServiceImpl extends ServiceImpl<EmployeeTransDetailMapper, EmployeeTransDetail> implements IEmployeeTransDetailService {
    @Autowired EmployeeTransDetailMapper detailMapper;

    @Override
    public EmployeeTransDetail queryPendingDetailById(Long id) {
        QueryWrapper<EmployeeTransDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("rel_pending_id",id);
        EmployeeTransDetail employeeTransDetail=detailMapper.selectOne(queryWrapper);
        return employeeTransDetail;
    }
}
