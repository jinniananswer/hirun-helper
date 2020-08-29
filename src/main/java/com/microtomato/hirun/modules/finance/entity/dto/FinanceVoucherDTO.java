package com.microtomato.hirun.modules.finance.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import lombok.Data;

import java.util.List;


@Data
public class FinanceVoucherDTO {

    private Long id;

    private Long orderId;

    private Long supplierId;

    /** 供应链id 针对材料付款使用 */
    @TableField(value = "supply_id")
    private Long supplyId;

    private Long voucherType;

    private Double totalMoney;

    private String auditStatus;

    private Long decoratorId;

    private Double money;

    private String remark;

    private String financeItemId;

    private String childFinanceItemId;

    private Long voucherId;

    /** 项目编码 可以为客户编码，师傅ID等 */
    @TableField(value = "project_id")
    private Long projectId;

    /** 费用科目，对应FinanceVoucher表ID**/
    @TableField(value = "voucher_item_id")
    private String voucherItemId;

    /** 上级费用科目，对应FinanceVoucher表ID**/
    @TableField(value = "parent_voucher_item_id")
    private String parentVoucherItemId;

//    /**
//     * 制单明细
//     */
//    private List<FinanceVoucherItemDTO> financeVoucherItemDTOList;
}
