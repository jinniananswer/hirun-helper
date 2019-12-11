package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeKeyman;
import com.microtomato.hirun.modules.organization.mapper.EmployeeKeymanMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeKeymanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-10
 */
@Slf4j
@Service
public class EmployeeKeymanServiceImpl extends ServiceImpl<EmployeeKeymanMapper, EmployeeKeyman> implements IEmployeeKeymanService {

    @Override
    public List<EmployeeKeyman> queryByEmployeeId(Long employeeId) {
        List<EmployeeKeyman> keyMen = this.list(new QueryWrapper<EmployeeKeyman>().lambda().eq(EmployeeKeyman::getEmployeeId, employeeId));
        return keyMen;
    }

    @Override
    public void deleteByEmployeeId(Long employeeId, String type) {
        this.remove(new QueryWrapper<EmployeeKeyman>().lambda().eq(EmployeeKeyman::getEmployeeId, employeeId).eq(EmployeeKeyman::getType, type));
    }

    @Override
    public void deleteByEmployeeId(Long employeeId) {
        this.remove(new QueryWrapper<EmployeeKeyman>().lambda().eq(EmployeeKeyman::getEmployeeId, employeeId));
    }
}
