package com.microtomato.hirun.modules.organization.service.impl;

import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTag;
import com.microtomato.hirun.modules.organization.mapper.EmployeeTagMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-05-04
 */
@Slf4j
@Service
public class EmployeeTagServiceImpl extends ServiceImpl<EmployeeTagMapper, EmployeeTag> implements IEmployeeTagService {

    @Override
    public void addEmployeeTag(String tagType, Long employeeId, String remark) {
        EmployeeTag employeeTag=new EmployeeTag();
        employeeTag.setTagType(tagType);
        employeeTag.setEmployeeId(employeeId);
        employeeTag.setRemark(remark);
        employeeTag.setStartTime(LocalDateTime.now());
        employeeTag.setEndTime(TimeUtils.getForeverTime());
        this.baseMapper.insert(employeeTag);
    }
}
