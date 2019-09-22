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
 * @author Steven
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_func")
public class Func extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "FUNC_ID", type = IdType.AUTO)
    private Integer funcId;

    @TableField("FUNC_CODE")
    private String funcCode;

    @TableField("TYPE")
    private String type;

    @TableField("FUNC_DESC")
    private String funcDesc;

    @TableField("STATUS")
    private String status;


}
