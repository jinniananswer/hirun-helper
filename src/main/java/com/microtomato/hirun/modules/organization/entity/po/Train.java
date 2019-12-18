package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * @author jinnian
 * @since 2019-12-17
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_train")
public class Train extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "train_id", type = IdType.AUTO)
    private Long trainId;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("train_desc")
    private String trainDesc;

    @TableField("train_address")
    private String trainAddress;

    @TableField("hotel_address")
    private String hotelAddress;

    @TableField("charge_employee_id")
    private Integer chargeEmployeeId;

    @TableField("status")
    private String status;

    @TableField("sign_status")
    private String signStatus;

    @TableField("sign_end_date")
    private LocalDateTime signEndDate;

    @TableField("remark")
    private String remark;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
