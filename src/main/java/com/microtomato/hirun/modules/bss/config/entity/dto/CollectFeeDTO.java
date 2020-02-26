package com.microtomato.hirun.modules.bss.config.entity.dto;

import com.microtomato.hirun.modules.bss.order.entity.dto.PaymentDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 收款业务数据传输对象
 * @author: jinnian
 * @create: 2020-02-26 00:00
 **/
@Data
public class CollectFeeDTO {

    private LocalDate payDate;

    private Double needPay;

    private Long orderId;

    private List<PayItemDTO> payItems;

    private List<PaymentDTO> payments;
}
