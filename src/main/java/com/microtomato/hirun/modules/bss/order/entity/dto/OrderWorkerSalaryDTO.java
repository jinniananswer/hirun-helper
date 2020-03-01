package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工人工资传输对象
 * @author: liuhui
 * @create: 2020-02-29
 **/
@Data
public class OrderWorkerSalaryDTO {

    private Long orderId;

    private Long id;

    private Integer periods;

    private Double hydropowerSalary=0.0;

    private String hydropowerRemark;

    private Double woodworkerSalary=0.0;

    private String woodworkerRemark;

    private Double tilerSalary=0.0;

    private String tilerRemark;

    private Double painterSalary=0.0;

    private String painterRemark;

    private Double wallworkerSalary=0.0;

    private String wallworkerRemark;

}
