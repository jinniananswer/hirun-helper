package com.microtomato.hirun.modules.bss.house.entity.po;

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
 * @author liuhui
 * @since 2020-02-11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_houses")
public class Houses extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "houses_id", type = IdType.AUTO)
    private Long housesId;

    @TableField("name")
    private String name;

    @TableField("city")
    private String city;

    @TableField("area")
    private String area;

    @TableField("org_id")
    private Long orgId;

    @TableField("nature")
    private Integer nature;

    @TableField("check_date")
    private LocalDateTime checkDate;

    @TableField("house_num")
    private Integer houseNum;

    @TableField("plan_counselor_num")
    private Integer planCounselorNum;

    @TableField("plan_in_date")
    private LocalDate planInDate;

    @TableField("actual_in_date")
    private LocalDate actualInDate;

    @TableField("destroy_date")
    private LocalDateTime destroyDate;

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


}
