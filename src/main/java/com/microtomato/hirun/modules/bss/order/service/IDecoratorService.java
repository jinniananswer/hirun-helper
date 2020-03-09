package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.ConstructionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

import java.util.List;

/**
 * <p>
 * 装修工人表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
public interface IDecoratorService extends IService<Decorator> {

    /**
     * 根据类型获取施工人员信息
     * @param orgId
     * @return
     */
    List<Decorator> queryDecoratorInfo(Long orgId, Long type);


}
