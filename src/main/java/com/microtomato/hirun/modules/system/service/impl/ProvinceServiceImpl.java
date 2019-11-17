package com.microtomato.hirun.modules.system.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Province;
import com.microtomato.hirun.modules.system.mapper.ProvinceMapper;
import com.microtomato.hirun.modules.system.service.IProvinceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements IProvinceService {

    /**
     * 获取所有省份数据
     * @return
     */
    @Override
    @Cacheable(value="all-provinces")
    public List<Province> getAllDatas() {
        return this.list();
    }

    /**
     * 根据省份ID获取省份数据
     * @param provinceId
     * @return
     */
    @Override
    @Cacheable(value="province-data")
    public Province getProvince(Integer provinceId) {
        IProvinceService provinceService = SpringContextUtils.getBean(ProvinceServiceImpl.class);
        List<Province> provinces = provinceService.getAllDatas();
        if (ArrayUtils.isEmpty(provinces)) {
            return null;
        }

        for (Province province : provinces) {
            if (StringUtils.equals(provinceId.toString(), province.getProvinceId())) {
                return province;
            }
        }
        return null;
    }
}
