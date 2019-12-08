package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuhui
 * @since 2019-11-05
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee_contract")
public class EmployeeContract extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("employee_id")
    private Long employeeId;

    /**
     * 合同类型
     */
    @TableField("contract_type")
    private String contractType;

    @TableField("contract_no")
    private String contractNo;

    @TableField("contract_sign_time")
    private LocalDateTime contractSignTime;

    @TableField("contract_start_time")
    private LocalDateTime contractStartTime;

    @TableField("contract_end_time")
    private LocalDateTime contractEndTime;

    @TableField("job_role")
    private String jobRole;

    @TableField("remark")
    private String remark;

    /**
     * 试用期
     */
    @TableField("probation")
    private String probation;

    /**
     * 户籍证明编号
     */
    @TableField("register_no")
    private String registerNo;

    /**
     * 房产证编号
     */
    @TableField("property_no")
    private String propertyNo;

    @TableField("parent_contract_id")
    private String parentContractId;

    @TableField("status")
    private String status;
    /**
     * 合同照片保存路径
     */
    @TableField("contract_path")
    private String contractPath;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
