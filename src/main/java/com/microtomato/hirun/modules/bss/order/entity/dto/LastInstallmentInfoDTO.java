package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 尾款收取中传输对象
 * @author: liuhui
 * @create: 2020-03-05
 **/
@Data
public class LastInstallmentInfoDTO {

    private Long orderId;

    private Double chargedAllFee=0.0;

    private Double chargedLastFee=0.0;

    private Double chargedMaterialFee=0.0;

    private Double chargedCupboardFee=0.0;

    private Double woodProductFee=0.0;

    private Double baseDecorationFee=0.0;

    private Double doorFee=0.0;

    private Double taxFee=0.0;

    private Double furnitureFee=0.0;

    private Double otherFee=0.0;

}
