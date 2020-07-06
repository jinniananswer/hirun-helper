package com.microtomato.hirun.modules.bss.order.entity.po;

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
 * 
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderConsult extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    /**
     * 客户代表
     */
    @TableField("cust_service_employee_id")
    private Long custServiceEmployeeId;

    /**
     * 设计师
     */
    @TableField("design_employee_id")
    private Long designEmployeeId;
    /**
     * 橱柜设计师
     */
    @TableField("design_cupboard_employee_id")
    private Long designCupboardEmployeeId;

    /**
     * 主材管家
     */
    @TableField("main_materialkeeper_employee_id")
    private Long mainMaterialKeeperEmployeeId;

    /**
     * 橱柜管家
     */
    @TableField("cupboard_keeper_employee_id")
    private Long cupboardKeeperEmployeeId;

    @TableField("consult_time")
    private LocalDateTime consultTime;

    @TableField("consult_remark")
    private String consultRemark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
