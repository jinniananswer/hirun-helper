package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 订单费用数据传输对象
 * @author: sunxin
 * @create: 2020-02-05
 **/
@Data
public class DecoratorInfoDTO {


    private Long orderId;

    private String decoratorTypeName;

    @TableId(value = "decorator_id", type = IdType.AUTO)
    private Long decoratorId;

    /**
     * 工人名字
     */
    @TableField("name")
    private String name;

    /**
     * 身份证号
     */
    @TableField("identity_no")
    private String identityNo;

    /**
     * 手机号码
     */
    @TableField("mobile_no")
    private String mobileNo;

    /**
     * 工人类别 见static表DECORATOR_TYPE
     */
    @TableField("decorator_type")
    private String decoratorType;

    /**
     * 协议标记，1代表有协议
     */
    @TableField("agreement_tag")
    private String agreementTag;

    /**
     * 老系统shifutable-hsid字段
     */
    @TableField("field1")
    private String field1;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 归属分公司
     */
    @TableField("org_id")
    private Long orgId;
}
