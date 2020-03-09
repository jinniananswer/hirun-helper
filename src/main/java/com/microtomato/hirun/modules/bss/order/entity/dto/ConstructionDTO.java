package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工程信息
 * @author: sunxin
 * @create: 2020-03-5 17:02
 **/
@Data
public class ConstructionDTO {

    private Long orderId;

    private Long custId;

    private Long roleId;

    private String auditStatus;

    private Long constructionId;

    private Long engineeringSupervisor;

    private Long projectManager;

    private Long engineeringAssistant;

    private Long plumberAndElectrician;

    private String plumberElectNum;

    private Long inlayer;

    private String inlayerNum;

    private Long carpentry;

    private String carpentryNum;

    private Long paperhanger;

    private String paperhangerNum;

    private Long wallKnocking;

    private String wallknockingNum;

    private String Remark;

    private String status;
}
