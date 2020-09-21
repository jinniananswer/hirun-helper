package com.microtomato.hirun.modules.bss.plan.entity.po;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * (PlanAgentMonth)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:26
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("plan_agent_month")
public class PlanAgentMonth extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "employee_id")
    private Long employeeId;


    @TableField(value = "month")
    private Integer month;

    /** 计划咨询数 */
    @TableField(value = "plan_consult_count")
    private Integer planConsultCount;

    /** 计划绑定客户代表数 */
    @TableField(value = "plan_bind_agent_count")
    private Integer planBindAgentCount;

    /** 计划风格蓝图数 */
    @TableField(value = "plan_style_count")
    private Integer planStyleCount;

    /** 计划功能蓝图A */
    @TableField(value = "plan_funca_count")
    private Integer planFuncaCount;

    /** 计划功能蓝图B */
    @TableField(value = "plan_funcb_count")
    private Integer planFuncbCount;

    /** 计划功能蓝图C */
    @TableField(value = "plan_funcc_count")
    private Integer planFunccCount;

    /** 计划城市木屋 */
    @TableField(value = "plan_citycabin_count")
    private Integer planCitycabinCount;

    /** 计划量房数 */
    @TableField(value = "plan_measure_count")
    private Integer planMeasureCount;

    /** 计划绑定设计师数 */
    @TableField(value = "plan_bind_design_count")
    private Integer planBindDesignCount;

    /** 部门id */
    @TableField(value = "org_id")
    private Long orgId;

    /** 门店ID */
    @TableField(value = "shop_id")
    private Long shopId;

    /** 分公司ID */
    @TableField(value = "company_id")
    private Long companyId;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}