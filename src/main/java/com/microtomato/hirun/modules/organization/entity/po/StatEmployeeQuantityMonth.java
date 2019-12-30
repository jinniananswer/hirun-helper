package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-12-17
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StatEmployeeQuantityMonth extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("org_id")
    private Long orgId;

    @TableField("year")
    private String year;

    @TableField("month")
    private String month;

    @TableField("employee_sum")
    private Integer employeeSum;

    @TableField("job_role")
    private String jobRole;

    @TableField("job_role_nature")
    private String jobRoleNature;


    @TableField("org_nature")
    private String orgNature;


    @TableField("less_month_quantity")
    private Integer lessMonthQuantity;

    @TableField("more_month_quantity")
    private Integer moreMonthQuantity;
}
