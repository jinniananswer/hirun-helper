package com.microtomato.hirun.modules.bi.middleproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodOpen;

import java.util.List;

/**
 * (MidprodOpen)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:46
 */
public interface IMidprodOpenService extends IService<MidprodOpen> {
        List<MidprodOpen> queryByOpenId(String openId);
}