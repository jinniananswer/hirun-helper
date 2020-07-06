package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 装修工人表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_decorator")
public class Decorator extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
