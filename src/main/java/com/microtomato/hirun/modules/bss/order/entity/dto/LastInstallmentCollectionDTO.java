package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 尾款收取中传输对象
 * @author: liuhui
 * @create: 2020-03-05
 **/
@Data
public class LastInstallmentCollectionDTO {

    private LastInstallmentInfoDTO lastInstallmentInfoDTO;

    private OrderWorkerSalaryDTO workerSalaryDTO;
}
