package com.microtomato.hirun.modules.bss.order.entity.po;

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
 * @since 2020-02-14
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderEscape extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    /**
     * 跑单节点
     */
    @TableField("escape_node")
    private String escapeNode;

    /**
     * 跑单类型
     */
    @TableField("escape_type")
    private String escapeType;

    /**
     * 跑单取向/趋势
     */
    @TableField("escape_trend")
    private String escapeTrend;

    /**
     * 跑单原因
     */
    @TableField("escape_cause")
    private String escapeCause;

    /**
     * 跑单时间
     */
    @TableField("escape_time")
    private LocalDate escapeTime;

    /**
     * 跑单具体原因
     */
    @TableField("escape_remark")
    private String escapeRemark;

    /**
     * 跑单状态1、未审核 2、已审核
     */
    @TableField("escape_status")
    private String escapeStatus;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
