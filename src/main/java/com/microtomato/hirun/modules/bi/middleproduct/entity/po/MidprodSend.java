package com.microtomato.hirun.modules.bi.middleproduct.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (MidprodSend)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:47
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_midprod_send")
public class MidprodSend extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;


    @TableField(value = "send_id")
    private String sendId;


    @TableField(value = "mode_id")
    private String modeId;


    @TableField(value = "stage")
    private String stage;


    @TableField(value = "title")
    private String title;


    @TableField(value = "auther")
    private String auther;


    @TableField(value = "company")
    private String company;


    @TableField(value = "department")
    private String department;


    @TableField(value = "content")
    private Object content;


    @TableField(value = "staff_id")
    private String staffId;

    @TableField(value = "staff_name")
    private String staffName;

    @TableField(value = "employee_id")
    private Long employeeId;


    @TableField(value = "org_id")
    private Long orgId;


    @TableField(value = "shop_id")
    private Long shopId;


    @TableField(value = "company_id")
    private Long companyId;


    @TableField(value = "send_time")
    private LocalDateTime sendTime;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}