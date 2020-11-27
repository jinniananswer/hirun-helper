package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 复核付款单据对象
 * @author: jinnian
 * @create: 2020-11-22 17:46
 **/
@Data
public class VoucherResultDTO {

    private Long id;

    private String voucherNo;

    private String type;

    private String typeName;

    private String item;

    private String itemName;

    private String auditStatus;

    private String auditStatusName;

    private LocalDate voucherDate;

    private Long voucherEmployeeId;

    private String voucherEmployeeName;

    private String remark;

    private Double totalMoney;

    private Long auditEmployeeId;

    private String auditEmployeeName;

    private String auditComment;

    private Long cashierEmployeeId;

    private String cashierEmployeeName;

    private LocalDate payDate;

    private List<VoucherItemResultDTO> voucherItems;
}
