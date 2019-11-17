package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.po.Province;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-17
 */
public interface IProvinceService extends IService<Province> {

    List<Province> getAllDatas();

    Province getProvince(Integer provinceId);
}
