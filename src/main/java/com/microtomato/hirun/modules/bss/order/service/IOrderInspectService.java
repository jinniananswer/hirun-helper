package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.QueryInspectCondDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;

/**
 * (OrderInspect)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
public interface IOrderInspectService extends IService<OrderInspect> {

    void save(OrderInspectDTO dto);

    OrderInspectDTO queryOrderInspect(Long orderId);

    void nextStep(OrderInspectDTO dto);

    void submitToNotReceive(OrderInspectDTO dto);

    /**
     * 申报信息查询
     * @param condDTO
     * @return
     */
    IPage<OrderInspectDTO> queryOrderInspects(QueryInspectCondDTO condDTO);
}