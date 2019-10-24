package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.HrPendingDO;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.IHrPendingDomainService;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @program: hirun-helper
 * @description: HR待办领域服务实现类
 **/
@Slf4j
@Service
public class HrPendingDomainServiceImpl implements IHrPendingDomainService {

    @Autowired
    private IHrPendingService hrPendingService;
    @Autowired
    private HrPendingDO hrPendingDO;

    @Override
    @DS("ins")
    public boolean addHrPending(HrPending hrPending) {
        UserContext userContext= WebContextUtils.getUserContext();
        hrPending.setPendingCreateId(userContext.getEmployeeId());
        hrPending.setPendingStatus("0");
        int result=hrPendingDO.add(hrPending);
        if( result<=0 ){
            return false;
        }
        return true;
    }

    @Override
    public IPage<HrPending> queryPendingByEmployeeId(Long employeeId, Page<HrPending> pendingPage) {

        IPage<HrPending> iPage=hrPendingService.queryPendingByEmployeeId(employeeId,pendingPage);
        //todo 翻译名字
        return iPage;
    }

    /**
     * 删除待办
     * @param hrPending
     * @return
     */
    @Override
    public boolean deleteHrPending(HrPending hrPending) {
        hrPending.setPendingStatus("3");
        return hrPendingDO.delete(hrPending);
    }


}
