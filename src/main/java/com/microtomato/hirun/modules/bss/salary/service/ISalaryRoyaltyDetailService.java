package com.microtomato.hirun.modules.bss.salary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;

import java.util.List;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
public interface ISalaryRoyaltyDetailService extends IService<SalaryRoyaltyDetail> {

    List<SalaryRoyaltyDetail> queryByOrderIdEmployeeIdItems(Long orderId, Long employeeId, List<String> items);
}