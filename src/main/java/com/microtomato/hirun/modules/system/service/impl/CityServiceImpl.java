package com.microtomato.hirun.modules.system.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.City;
import com.microtomato.hirun.modules.system.mapper.CityMapper;
import com.microtomato.hirun.modules.system.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-17
 */
@Slf4j
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

    /**
     * 获取所有城市数据
     * @return
     */
    @Override
    @Cacheable(value="all-citys")
    public List<City> getAllDatas() {
        return this.list();
    }

    /**
     * 根据城市ID获取城市数据
     * @param cityId
     * @return
     */
    @Override
    @Cacheable(value="city-data")
    public City getCity(Integer cityId) {
        ICityService cityService = SpringContextUtils.getBean(CityServiceImpl.class);
        List<City> citys = cityService.getAllDatas();
        if (ArrayUtils.isEmpty(citys)) {
            return null;
        }

        for (City city : citys) {
            if (StringUtils.equals(cityId.toString(), city.getCityId())) {
                return city;
            }
        }
        return null;
    }
}
