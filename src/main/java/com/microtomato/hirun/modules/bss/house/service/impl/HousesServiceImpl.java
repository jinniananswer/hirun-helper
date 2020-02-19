package com.microtomato.hirun.modules.bss.house.service.impl;

import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.mapper.HousesMapper;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
@Slf4j
@Service
public class HousesServiceImpl extends ServiceImpl<HousesMapper, Houses> implements IHousesService {

    @Override
    public String queryHouseName(Long houseId) {
        Houses houses = this.baseMapper.selectById(houseId);
        if (houses == null) {
            return "";
        }
        return houses.getName();
    }

    /**
     * 根据楼盘ID查询楼盘信息
     * @param houseId
     * @return
     */
    @Override
    public Houses getHouse(Long houseId) {
        Houses house = this.getById(houseId);
        return house;
    }
}
