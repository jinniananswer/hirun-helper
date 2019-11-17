package com.microtomato.hirun.modules.system.entity.domain;

import com.microtomato.hirun.modules.system.entity.po.City;
import com.microtomato.hirun.modules.system.entity.po.County;
import com.microtomato.hirun.modules.system.entity.po.Province;
import com.microtomato.hirun.modules.system.service.ICityService;
import com.microtomato.hirun.modules.system.service.ICountyService;
import com.microtomato.hirun.modules.system.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 地址信息领域对象
 * @author: jinnian
 * @create: 2019-11-17 14:50
 **/
@Component
public class AddressDO {

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICountyService countyService;

    /**
     * 获取省份名字
     * @param provinceId
     * @return
     */
    public String getProvinceName(Integer provinceId) {
        if (provinceId != null) {
            Province province = this.provinceService.getProvince(provinceId);
            if (province != null) {
                return province.getName();
            }
        }
        return "";
    }

    /**
     * 获取城市名称
     * @param cityId
     * @return
     */
    public String getCityName(Integer cityId) {
        if (cityId != null) {
            City city = this.cityService.getCity(cityId);
            if (city != null) {
                return city.getName();
            }
        }
        return "";
    }

    /**
     * 获取区域名称
     * @param countyId
     * @return
     */
    public String getCountyName(Integer countyId) {
        if (countyId != null) {
            County county = this.countyService.getCounty(countyId);
            if (county != null) {
                return county.getName();
            }
        }
        return "";
    }

    /**
     * 根据传入的省、市、区ID获取省市区的组合名字
     * @param countyId
     * @return
     */
    @Cacheable(value="area-full-name")
    public String getFullName(Integer countyId) {
        if (countyId == null) {
            return "";
        }
        County county = this.countyService.getCounty(countyId);
        if (county != null) {
            Integer cityId = Integer.parseInt(county.getCityId());
            City city = this.cityService.getCity(cityId);
            Province province = this.provinceService.getProvince(Integer.parseInt(city.getProvinceId()));

            return province.getName() + "/" + city.getName() + "/" + county.getName();
        }

        return "";
    }
}
