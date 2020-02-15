package com.microtomato.hirun.modules.bss.house.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.house.entity.po.HousesPlan;
import com.microtomato.hirun.modules.bss.house.mapper.HousesPlanMapper;
import com.microtomato.hirun.modules.bss.house.service.IHousesPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-11
 */
@Slf4j
@Service
public class HousesPlanServiceImpl extends ServiceImpl<HousesPlanMapper, HousesPlan> implements IHousesPlanService {

    @Override
    public HousesPlan queryHousesPlan(Long houseId, Long employeeId) {
        HousesPlan housesPlan=this.baseMapper.selectOne(new QueryWrapper<HousesPlan>().lambda()
                .eq(HousesPlan::getHousesId,houseId)
                .eq(HousesPlan::getEmployeeId,employeeId)
                .apply("now() between start_date and end_date "));
        return null;
    }
}
