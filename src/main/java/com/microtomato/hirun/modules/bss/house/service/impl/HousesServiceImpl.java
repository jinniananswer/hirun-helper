package com.microtomato.hirun.modules.bss.house.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.mapper.HousesMapper;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private IStaticDataService staticDataService;

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

    @Override
    public List<Houses> queryAll() {
        List<Houses> houses = this.list(Wrappers.<Houses>lambdaQuery()
                .select(Houses::getHousesId, Houses::getName, Houses::getNature, Houses::getRemark, Houses::getCity)
                .in(Houses::getNature, "0", "1", "2")
                .orderByAsc(Houses::getCity, Houses::getNature));

        if (ArrayUtils.isEmpty(houses)) {
            return null;
        }

        houses.forEach(house -> {
            String natureName = staticDataService.getCodeName("HOUSE_NATURE",house.getNature()+"");
            String cityName = this.staticDataService.getCodeName("BIZ_CITY", house.getCity());
            house.setRemark(cityName + " | " + natureName);
        });

        return houses;
    }
}
