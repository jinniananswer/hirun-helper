package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class StatEmployeeQuantityMonthServiceImpl extends ServiceImpl<StatEmployeeQuantityMonthMapper, StatEmployeeQuantityMonth> implements IStatEmployeeQuantityMonthService {
    @Autowired
    private StatEmployeeQuantityMonthMapper mapper;

    @Override
    public StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId) {
        StatEmployeeQuantityMonth statEmployeeQuantityMonth= this.mapper.selectOne(new QueryWrapper<StatEmployeeQuantityMonth>().lambda()
                .eq(StatEmployeeQuantityMonth::getYear,year)
                .eq(StatEmployeeQuantityMonth::getOrgId,orgId).eq(StatEmployeeQuantityMonth::getMonth,month));
        return statEmployeeQuantityMonth;
    }

    @Override
    public IPage<StatEmployeeQuantityMonth> queryEmployeeQuantityStat(String year, String orgId, Page page) {
        QueryWrapper<StatEmployeeQuantityMonth> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("year",year);
        queryWrapper.eq(StringUtils.isNotEmpty(orgId),"orgId",orgId);
        IPage<StatEmployeeQuantityMonth> iPage=this.mapper.selectPage(page,queryWrapper);
        if(iPage.getRecords().size()<=0){
            return iPage;
        }
        return this.mapper.selectPage(page,queryWrapper);
    }
}
