package com.microtomato.hirun.modules.bss.house.service;

import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
public interface IHousesService extends IService<Houses> {
    String queryHouseName(Long houseId);

    Houses queryHouse(Long houseId);
}
