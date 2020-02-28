package com.microtomato.hirun.modules.bss.config.entity.dto;

import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.microtomato.hirun.modules.bss.order.entity.dto.PaymentDTO;
import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-02-23 12:47
 **/
@Data
public class PayComponentDTO {

    private List<PaymentDTO> payments;

    private List<CascadeDTO<PayItemCfg>> payItemOption;
}
