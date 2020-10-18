package com.microtomato.hirun.modules.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (FinanceAcct)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-10-13 16:51:29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("finance_acct")
public class FinanceAcct extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    /** 付款方式ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "type")
    private String type;

    @TableField(value = "name")
    private String name;

    /** 公司ID */
    @TableField(value = "company_id")
    private String companyId;

    /** 使用店面 */
    @TableField(value = "use_shop_id")
    private String useShopId;

    /** 使用员工角色 */
    @TableField(value = "use_role_id")
    private String useRoleId;

    /** 使用员工角色 */
    @TableField(value = "status")
    private String status;
}