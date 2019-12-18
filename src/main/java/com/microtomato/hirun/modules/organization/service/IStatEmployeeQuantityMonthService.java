package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
public interface IStatEmployeeQuantityMonthService extends IService<StatEmployeeQuantityMonth> {
    /**
     * 查询统计记录
     * @param year
     * @param month
     * @param orgId
     * @return
     */
        StatEmployeeQuantityMonth queryCountRecord(String year,String month,Long orgId);

        IPage<StatEmployeeQuantityMonth> queryEmployeeQuantityStat(String year,String orgId,Page page);
}
