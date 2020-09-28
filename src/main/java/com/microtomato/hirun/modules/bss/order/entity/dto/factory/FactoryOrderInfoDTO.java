package com.microtomato.hirun.modules.bss.order.entity.dto.factory;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 工厂订单查询数据对象
 * @author: jinnian
 * @create: 2020-09-26 19:21
 **/
@Data
public class FactoryOrderInfoDTO {

    private Long orderId;

    private Long custId;

    private String produceNo;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String status;

    private String orderStatusName;

    private String factoryStatus;

    private String factoryStatusName;

    private LocalDate contractStartDate;

    private UsualOrderWorkerDTO usualWorker;

}
