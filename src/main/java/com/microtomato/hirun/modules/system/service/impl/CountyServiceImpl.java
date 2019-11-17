package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.County;
import com.microtomato.hirun.modules.system.mapper.CountyMapper;
import com.microtomato.hirun.modules.system.service.ICountyService;
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
public class CountyServiceImpl extends ServiceImpl<CountyMapper, County> implements ICountyService {

    /**
     * 获取所有区域数据
     * @return
     */
    @Override
    @Cacheable(value="all-counties")
    public List<County> getAllDatas() {
        return null;
    }

    /**
     * 根据区域ID获取区域数据
     * @param countyId
     * @return
     */
    @Override
    @Cacheable(value="county-data")
    public County getCounty(Integer countyId) {
        ICountyService countyService = SpringContextUtils.getBean(CountyServiceImpl.class);
        List<County> counties = countyService.getAllDatas();
        if (ArrayUtils.isEmpty(counties)) {
            return null;
        }

        for (County county : counties) {
            if (countyId.equals(county.getId())) {
                return county;
            }
        }
        return null;
    }
}
