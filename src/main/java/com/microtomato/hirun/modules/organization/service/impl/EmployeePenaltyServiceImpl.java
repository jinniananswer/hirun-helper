package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePenaltyDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePenalty;
import com.microtomato.hirun.modules.organization.mapper.EmployeePenaltyMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeePenaltyService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-14
 */
@Slf4j
@Service
public class EmployeePenaltyServiceImpl extends ServiceImpl<EmployeePenaltyMapper, EmployeePenalty> implements IEmployeePenaltyService {

    @Autowired
    EmployeePenaltyMapper penaltyMapper;

    @Autowired
    private IStaticDataService staticDataService;
    //正常状态
    public static final String PENALTY_STATUS_NORMAL = "1";
    //删除状态
    public static final String PENALTY_STATUS_DELETE = "2";


    @DataSource(DataSourceKey.INS)
    @Override
    public void addEmployeePenalty(EmployeePenalty employeePenalty) {
        employeePenalty.setPenaltyStatus(PENALTY_STATUS_NORMAL);
        penaltyMapper.insert(employeePenalty);
    }

    @Override
    public IPage<EmployeePenaltyDTO> queryPenaltyList(EmployeePenaltyDTO employeePenaltyDTO, Page<EmployeePenaltyDTO> page) {
        QueryWrapper<EmployeePenaltyDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id=a.employee_id AND (now() between b.start_date and b.end_date) AND c.org_id = b.org_id and a.employee_id=d.employee_id and d.penalty_status='1'");
        queryWrapper.like(StringUtils.isNotEmpty(employeePenaltyDTO.getEmployeeName()), "a.name", employeePenaltyDTO.getEmployeeName());
        queryWrapper.eq(StringUtils.isNotEmpty(employeePenaltyDTO.getPenaltyType()), "d.penalty_type", employeePenaltyDTO.getPenaltyType());
        queryWrapper.orderByDesc("d.create_time");

        IPage<EmployeePenaltyDTO> iPage = penaltyMapper.selectEmployeePenaltyPage(page, queryWrapper);
        if (iPage.getRecords().size() <= 0) {
            return iPage;
        }
        List<EmployeePenaltyDTO> list = new ArrayList<>();
        for (EmployeePenaltyDTO penaltyDTO : iPage.getRecords()) {
            penaltyDTO.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", penaltyDTO.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, penaltyDTO.getOrgId());
            penaltyDTO.setOrgPath(orgDO.getCompanyLinePath());
            list.add(penaltyDTO);
        }
        return iPage.setRecords(list);
    }

    @Override
    public boolean deleteEmployeePenalty(EmployeePenalty employeePenalty) {
        employeePenalty.setPenaltyStatus(PENALTY_STATUS_DELETE);
        int result = penaltyMapper.updateById(employeePenalty);
        if (result <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEmployeePenalty(EmployeePenalty employeePenalty) {
        int result = this.penaltyMapper.updateById(employeePenalty);
        if (result <= 0) {
            return false;
        }
        return true;
    }

}
