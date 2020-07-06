package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.City;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-17
 */
public interface ICityService extends IService<City> {

    List<City> getAllDatas();

    City getCity(Integer cityId);

    @Async
    Future<City> getCityByIdAsync(Integer cityId);
}
