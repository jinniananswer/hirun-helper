package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description: 会计收单对象
 * @author: jinnian
 * @create: 2020-11-02 00:51
 **/
@Getter
@Setter
public class ReceiveReceiptDTO {

    private Long orderId;

    private Long payNo;

    private String auditStatus;

    private String receiveComment;
}
