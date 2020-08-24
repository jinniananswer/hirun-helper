package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工地未收齐款项查询结果
 * @author: jinnian
 * @create: 2020-07-20 23:47
 **/
@Data
public class NoBalanceFeeDTO {

    private Long orderId;

    private Long custId;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String type;

    private String status;

    private String orderStatusName;

    private String feeType;

    private String feeTypeName;

    private Integer periods;

    private String periodsName;

    private Double needPay;

    private Double pay;

    private Double minus;

    private String remark;

    private Long shopId;

    private String shopName;

    private UsualOrderWorkerDTO usualWorker;
}
