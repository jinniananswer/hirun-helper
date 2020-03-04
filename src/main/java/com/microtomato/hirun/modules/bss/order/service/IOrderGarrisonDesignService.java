package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderGarrisonDesign;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-04
 */
public interface IOrderGarrisonDesignService extends IService<OrderGarrisonDesign> {
   void  saveGarrisonDesignInfo(OrderGarrisonDesign orderGarrisonDesign);

    void  submitBudget(OrderGarrisonDesign orderGarrisonDesign);

}
