package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 非主营支付流水表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
public interface INormalPayNoService extends IService<NormalPayNo> {

    NormalPayNo getByPayNo(Long payNo);

}
