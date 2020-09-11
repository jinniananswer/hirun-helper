package com.microtomato.hirun.modules.bss.salary.entity.domain;

import com.microtomato.hirun.framework.util.ArrayUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 费用数据类
 * @author: jinnian
 * @create: 2020-05-17 23:12
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeFact {

    /**
     * 费用类型 设计费、工程款、主材、橱柜
     */
    private String type;

    /**
     * 期数
     */
    private Integer periods;

    /**
     * 费用子项
     */
    private List<FeeItemFact> items;

    /**
     * 合同总金额
     */
    private Long totalFee;

    /**
     * 应收
     */
    private Long needPay;

    /**
     * 实收
     */
    private Long pay;

    /**
     * 获取设计费
     * @return
     */
    public Long getDesignFee() {
        if (StringUtils.equals("1", type)) {
            return totalFee;
        }

        return 0L;
    }

    public Long getContractFee() {
        if (StringUtils.equals("2", type)) {
            return totalFee;
        }
        return 0L;
    }

    /**
     * 获取基础装修款
     * @return
     */
    public Long getBasicFee() {
        return this.getFeeByTypeItemId("2", 5L);
    }

    /**
     * 获取门费用
     * @return
     */
    public Long getDoorFee() {
        return this.getFeeByTypeItemId("2", 8L);
    }

    /**
     * 获取家具费用
     * @return
     */
    public Long getFurnitureFee() {
        return this.getFeeByTypeItemId("2", 7L);
    }

    /**
     * 根据类型与费用项编码获取费用
     * @param type
     * @param feeItemId
     * @return
     */
    private Long getFeeByTypeItemId(String type, Long feeItemId) {
        if (StringUtils.equals(this.type, type)) {
            if (ArrayUtils.isEmpty(items)) {
                return 0L;
            }

            for (FeeItemFact item : items) {
                if (item.getFeeItemId().equals(feeItemId)) {
                    return item.getFee();
                }
            }
        }

        return 0L;
    }
}
