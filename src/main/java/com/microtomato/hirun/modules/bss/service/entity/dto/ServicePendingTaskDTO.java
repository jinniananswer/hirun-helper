package com.microtomato.hirun.modules.bss.service.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 服务中心待办数据传输对象
 * @author: liuhui7
 * @create: 2020-07-21 14:45
 **/
@Data
public class ServicePendingTaskDTO {

    private String statusName;

    /**
     * 区分是维修还是投诉
     */
    private String type;

    private String status;

    private List<ServicePendingOrderDTO> orders;
}
