package com.microtomato.hirun.modules.demo.service;

import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
public interface IStevenService extends IService<Steven> {
    void batchInsert(List<Steven> list);
}
