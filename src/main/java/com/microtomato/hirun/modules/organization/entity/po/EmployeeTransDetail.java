package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
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
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee_trans_detail")
public class EmployeeTransDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("rel_pending_id")
    private Long relPendingId;

    @TableField("employee_id")
    private Long employeeId;

    @TableField("b_org_id")
    private Long bOrgId;

    @TableField("b_job_role")
    private String bJobRole;

    @TableField("b_job_nature")
    private String bJobNature;

    @TableField("b_discount_rate")
    private String bDiscountRate;

    @TableField("b_parent_employee_id")
    private Long bParentEmployeeId;

    @TableField("b_home_prov")
    private Integer bHomeProv;

    @TableField("b_home_city")
    private Integer bHomeCity;

    @TableField("b_home_region")
    private Integer bHomeRegion;

    @TableField("b_home_address")
    private String bHomeAddress;

    @TableField("a_org_id")
    private Long aOrgId;

    @TableField("a_job_role")
    private String aJobRole;

    @TableField("a_job_nature")
    private String aJobNature;

    @TableField("a_discount_rate")
    private String aDiscountRate;

    @TableField("a_parent_employee_id")
    private Long aParentEmployeeId;

    @TableField("a_home_prov")
    private Integer aHomeProv;

    @TableField("a_home_city")
    private Integer aHomeCity;

    @TableField("a_home_region")
    private Integer aHomeRegion;

    @TableField("a_home_address")
    private String aHomeAddress;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
