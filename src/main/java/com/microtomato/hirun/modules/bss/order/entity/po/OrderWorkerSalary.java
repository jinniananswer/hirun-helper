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
 * @since 2020-03-01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderWorkerSalary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("periods")
    private Integer periods;

    /**
     * 水电工工资
     */
    @TableField("hydropower_salary")
    private Long hydropowerSalary;

    /**
     * 水电备注
     */
    @TableField("hydropower_remark")
    private String hydropowerRemark;

    /**
     * 木工工资
     */
    @TableField("woodworker_salary")
    private Long woodworkerSalary;

    /**
     * 木工备注
     */
    @TableField("woodworker_remark")
    private String woodworkerRemark;

    /**
     * 泥工工资
     */
    @TableField("tiler_salary")
    private Long tilerSalary;

    @TableField("tiler_remark")
    private String tilerRemark;

    /**
     * 油漆工资
     */
    @TableField("painter_salary")
    private Long painterSalary;

    /**
     * 油漆
     */
    @TableField("painter_remark")
    private String painterRemark;

    /**
     * 敲墙工人
     */
    @TableField("wallworker_salary")
    private Long wallworkerSalary;

    @TableField("wallworker_remark")
    private String wallworkerRemark;

    /**
     * 1、未保存 2、已保存
     */
    @TableField("status")
    private String status;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
