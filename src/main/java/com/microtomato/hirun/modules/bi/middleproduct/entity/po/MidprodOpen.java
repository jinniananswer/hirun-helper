package com.microtomato.hirun.modules.bi.middleproduct.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (MidprodOpen)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:46
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_midprod_open")
public class MidprodOpen extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;


    @TableField(value = "send_id")
    private Long sendId;


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


    @TableField(value = "open_time")
    private LocalDateTime openTime;

    @TableField(value = "staff_name")
    private String staffName;

    @TableField(value = "open_id")
    private String openId;


    @TableField(value = "wx_nick")
    private String wxNick;


    @TableField(value = "cust_name")
    private String custName;


    @TableField(value = "house_id")
    private Long houseId;


    @TableField(value = "head_url")
    private String headUrl;



    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}