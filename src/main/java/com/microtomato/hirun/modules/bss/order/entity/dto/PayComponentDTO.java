package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-02-23 12:47
 **/
@Data
public class PayComponentDTO {

    private LocalDate payDate;

    private Double needPay;

    private String auditComment;

    private String auditStatus;

    private String remark;

    private List<PaymentDTO> payments;

    private List<PayItemDTO> payItems;

    private List<CascadeDTO<PayItemCfg>> payItemOption;
}
