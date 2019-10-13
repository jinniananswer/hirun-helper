package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author jinnian
 * @since 2019-09-14
 */
@Data
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
    @TableId(value = "DATA_ID", type = IdType.AUTO)
    private Long dataId;

    /**
     * 员工工号
     */
    @TableField("CODE_TYPE")
    private String codeType;

    @TableField("CODE_VALUE")
    private String codeValue;

    @TableField("CODE_NAME")
    private String codeName;

    @TableField("SORT_NO")
    private Integer sortNo;

    @TableField("CODE_DESC")
    private String codeDesc;

    @TableField("STATUS")
    private String status;


}
