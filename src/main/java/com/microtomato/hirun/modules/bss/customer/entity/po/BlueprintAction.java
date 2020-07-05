package com.microtomato.hirun.modules.bss.customer.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-04-28
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_blueprint_action")
public class BlueprintAction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "blueprint_action_id", type = IdType.AUTO)
    private Long blueprintActionId;

    @TableField("open_id")
    private String openId;

    @TableField("action_code")
    private String actionCode;

    @TableField("mode_id")
    private String modeId;

    @TableField("mode_time")
    private String modeTime;

    @TableField("name")
    private String name;

    @TableField("age")
    private String age;

    @TableField("house_kind")
    private String houseKind;

    /**
     * 户型面积
     */
    @TableField("house_area")
    private String houseArea;

    @TableField("application")
    private String application;

    /**
     * 风格
     */
    @TableField("style")
    private String style;

    @TableField("func")
    private String func;

    @TableField("func_b")
    private String funcB;

    @TableField("func_c")
    private String funcC;

    @TableField("staff_id")
    private String staffId;

    @TableField("xqlte_create_time")
    private LocalDateTime xqlteCreateTime;

    @TableField("xqlte_update_time")
    private LocalDateTime xqlteUpdateTime;

    @TableField("funcprint_create_time")
    private LocalDateTime funcprintCreateTime;

    @TableField("funcprint_update_time")
    private LocalDateTime funcprintUpdateTime;

    @TableField("styleprint_create_time")
    private LocalDateTime styleprintCreateTime;

    @TableField("styleprint_update_time")
    private LocalDateTime styleprintUpdateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("rel_employee_id")
    private Long relEmployeeId;


}
