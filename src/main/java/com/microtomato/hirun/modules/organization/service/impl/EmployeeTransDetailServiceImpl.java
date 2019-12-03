package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.consts.HrPendingConst;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.mapper.EmployeeTransDetailMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeTransDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-29
 */
@Slf4j
@Service
public class EmployeeTransDetailServiceImpl extends ServiceImpl<EmployeeTransDetailMapper, EmployeeTransDetail> implements IEmployeeTransDetailService {

    @Autowired
    EmployeeTransDetailMapper detailMapper;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IHrPendingService hrPendingService;

    @Override
    public EmployeeTransDetail queryPendingDetailById(Long id) {
        QueryWrapper<EmployeeTransDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rel_pending_id", id);
        EmployeeTransDetail employeeTransDetail = detailMapper.selectOne(queryWrapper);
        return employeeTransDetail;
    }

    @Override
    public List<EmployeeTransDetail> queryVaildTransDetail(String type) {
        QueryWrapper<EmployeeTransDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "trans_type", type);
        queryWrapper.apply("now() between start_time and end_time ");
        return this.detailMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean confirmReturnDetail(EmployeeTransDetail transDetail) {
        Employee employee = new Employee();
        EmployeeJobRole employeeJobRole = new EmployeeJobRole();

        BeanUtils.copyProperties(transDetail, employeeJobRole);
        BeanUtils.copyProperties(transDetail, employee);
        //修改employee现居住地址
        EmployeeDO employeeDo = SpringContextUtils.getBean(EmployeeDO.class, transDetail.getEmployeeId());
        employeeDo.modify(employee, null, null, null);

        //结束原来生效的jobrole数据
        employeeDo.destroyJob(LocalDateTime.now());
        //新增员工jobRole数据
        employeeJobRole.setStartDate(LocalDateTime.now());
        employeeJobRole.setEndDate(TimeUtils.getForeverTime());
        employeeJobRole.setIsMain(EmployeeConst.JOB_ROLE_MAIN);
        employeeJobRoleService.save(employeeJobRole);

        //更新待办
        HrPending hrPending = new HrPending();
        hrPending.setId(transDetail.getRelPendingId());
        hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_2);

        hrPendingService.updateById(hrPending);

        //更新确认详细
        int result = detailMapper.updateById(transDetail);
        if (result > 0) {
            return true;
        }
        return false;
    }
}
