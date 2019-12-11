package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jinnian
 * @since 2019-12-10
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee_keyman")
public class EmployeeKeyman extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("employee_id")
    private Long employeeId;

    /**
     * 见静态参数表类型KEYMAN_TYPE
     */
    @TableField("type")
    private String type;

    /**
     * 见静态参数表类型KEYMAN_REL_TYPE
     */
    @TableField("rel_type")
    private String relType;

    @TableField("name")
    private String name;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("sex")
    private String sex;

    @TableField("identity_no")
    private String identityNo;

    @TableField("contact_no")
    private String contactNo;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
