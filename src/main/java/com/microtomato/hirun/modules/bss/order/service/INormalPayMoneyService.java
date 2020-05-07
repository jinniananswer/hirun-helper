package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayMoney;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 非主营付款类型明细表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
public interface INormalPayMoneyService extends IService<NormalPayMoney> {

    List<NormalPayMoney> queryByPayNo(Long payNo);

}
