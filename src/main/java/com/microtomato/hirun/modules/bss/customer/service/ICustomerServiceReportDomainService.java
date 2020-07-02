package com.microtomato.hirun.modules.bss.customer.service;


import com.microtomato.hirun.modules.bss.customer.entity.dto.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
public interface ICustomerServiceReportDomainService {

    /**
     * 查询门店报表
     */

    Map<String,List> queryReport(ReportQueryCondDTO param);

    /**
     * 查询门店报表
     */

    Map<String,List> queryAgentPlanAcutalReport(ReportQueryCondDTO param);
}
