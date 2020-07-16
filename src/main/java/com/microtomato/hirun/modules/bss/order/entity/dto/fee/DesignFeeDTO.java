package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 设计费用查询结果数据对象
 * @author: jinnian
 * @create: 2020-07-04 15:11
 **/
@Data
public class DesignFeeDTO {

    private Long orderId;

    private Long custId;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String type;

    private String status;

    private String orderStatusName;

    private String houseLayout;

    private String houseLayoutName;

    private String indoorArea;

    private Integer designFeeStandard;

    private Long designFee;

    private String designTheme;

    private Long feeNo;

    private Long depositFee;

    private LocalDateTime feeTime;

    private String remark;

    private LocalDateTime signTime;

    private LocalDateTime firstPayTime;

    private Long shopId;

    private String shopName;

    private String depositFinanceName;

    private String designFeeFinanceName;

    private UsualOrderWorkerDTO usualWorker;

    private String contractTime;
}
