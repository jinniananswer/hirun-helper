package com.microtomato.hirun.modules.finance.entity.dto;

import com.microtomato.hirun.modules.bss.order.entity.dto.PaymentDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 付款流水数据对象
 * @author: jinnian
 * @create: 2020-11-24 17:32
 **/
@Data
public class FinancePayNoDTO {

    private Long id;

    private Long payNo;

    private LocalDate payDate;

    private String billNo;

    private String voucherNo;

    private Double totalMoney;

    private String remark;

    private List<PaymentDTO> payments;
}
