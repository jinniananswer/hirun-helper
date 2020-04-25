package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description:
 * @author: sunxin
 * @create: 2020-03-9 12:47
 **/
@Data
public class CollectionComponentDTO {

    private LocalDate payDate;

    private Double needCollection;

    private List<PaymentDTO> payments;

    private List<NormalPayItemDTO> payItems;

    private Double needPay;

    private List<CascadeDTO<CollectionItemCfg>> collectionItemOption;

    private String auditStatus;
}
