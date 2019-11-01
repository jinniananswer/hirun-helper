package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import com.microtomato.hirun.modules.organization.mapper.EmployeeWorkExperienceMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeWorkExperienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-10-19
 */
@Slf4j
@Service
public class EmployeeWorkExperienceServiceImpl extends ServiceImpl<EmployeeWorkExperienceMapper, EmployeeWorkExperience> implements IEmployeeWorkExperienceService {

    @Override
    public List<EmployeeWorkExperience> queryByEmployeeId(Long employeeId) {
        LambdaQueryWrapper<EmployeeWorkExperience> wrapper = new LambdaQueryWrapper<EmployeeWorkExperience>();
        wrapper.eq(EmployeeWorkExperience::getEmployeeId, employeeId);
        return this.list(wrapper);
    }
}
