package com.microtomato.hirun.modules.bss.house.service;

import com.microtomato.hirun.modules.bss.house.entity.po.HousesPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
public interface IHousesPlanService extends IService<HousesPlan> {
    /**
     * 查询楼盘规划信息
     * @param houseId
     * @param employeeId
     * @return
     */
    HousesPlan queryHousesPlan(Long houseId,Long employeeId);
}
