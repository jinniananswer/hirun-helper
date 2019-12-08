package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jinnian
 * @since 2019-09-14
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_static_data")
public class StaticData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID，唯一主键
     */
    @TableId(value = "data_id", type = IdType.AUTO)
    private Long dataId;

    /**
     * 员工工号
     */
    @TableField("code_type")
    private String codeType;

    @TableField("code_value")
    private String codeValue;

    @TableField("code_name")
    private String codeName;

    @TableField("sort_no")
    private Integer sortNo;

    @TableField("code_desc")
    private String codeDesc;

    @TableField("status")
    private String status;


}
