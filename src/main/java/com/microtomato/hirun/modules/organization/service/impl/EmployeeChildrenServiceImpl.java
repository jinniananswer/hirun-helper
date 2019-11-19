package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeChildren;
import com.microtomato.hirun.modules.organization.mapper.EmployeeChildrenMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeChildrenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-19
 */
@Slf4j
@Service
public class EmployeeChildrenServiceImpl extends ServiceImpl<EmployeeChildrenMapper, EmployeeChildren> implements IEmployeeChildrenService {

    /**
     * 根据员工ID查询员工子女信息
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeChildren> queryByEmployeeId(Long employeeId) {
        List<EmployeeChildren> children = this.list(new QueryWrapper<EmployeeChildren>().lambda().eq(EmployeeChildren::getEmployeeId, employeeId));
        return children;
    }

    /**
     * 根据员工ID删除员工子女信息
     * @param employeeId
     */
    @Override
    public void deleteByEmployeeId(Long employeeId) {
        this.remove(new QueryWrapper<EmployeeChildren>().lambda().eq(EmployeeChildren::getEmployeeId, employeeId));
    }
}
