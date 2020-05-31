package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryRoyaltyDetailMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
@Service
@DataSource(DataSourceKey.INS)
@Slf4j
public class SalaryRoyaltyDetailServiceImpl extends ServiceImpl<SalaryRoyaltyDetailMapper, SalaryRoyaltyDetail> implements ISalaryRoyaltyDetailService {

    @Autowired
    private SalaryRoyaltyDetailMapper salaryRoyaltyDetailMapper;
    

}