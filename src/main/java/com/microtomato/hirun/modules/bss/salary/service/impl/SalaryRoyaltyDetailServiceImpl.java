package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryRoyaltyDetailMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 根据订单ID、员工ID，提成明细项列表查询用户提成详情信息
     * @param orderId
     * @param employeeId
     * @param items
     * @return
     */
    @Override
    public List<SalaryRoyaltyDetail> queryByOrderIdEmployeeIdItems(Long orderId, Long employeeId, List<String> items) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<SalaryRoyaltyDetail>lambdaQuery()
                .eq(SalaryRoyaltyDetail::getOrderId, orderId)
                .eq(SalaryRoyaltyDetail::getEmployeeId, employeeId)
                .in(SalaryRoyaltyDetail::getItem, items)
                .ge(SalaryRoyaltyDetail::getEndTime, now));
    }
    

}