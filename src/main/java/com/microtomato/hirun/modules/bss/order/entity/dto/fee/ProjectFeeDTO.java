package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 查询工程费用数据返回对象
 * @author: jinnian
 * @create: 2020-07-12 02:07
 **/
@Data
public class ProjectFeeDTO {

    private Long orderId;

    private Long custId;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String type;

    private String status;

    private String orderStatusName;

    private String houseLayout;

    private String houseLayoutName;

    private String indoorArea;

    private UsualOrderWorkerDTO usualWorker;

    private UsualFeeDTO usualFee;

    private String reportStatusName;

    private Double backDesignFee;

    private Double budgetBasic;

    private Double budgetDoor;

    private Double budgetFurniture;

    private String discountItem;

    private String saleActive;

    private String financeRemark;

    private LocalDate submitFileDate;

    private LocalDate submitReportDate;

    private LocalDate fetchRoyaltyDate;

    private String businessLevel;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private String firstPayFull;

    private String secondPayFull;

    private String settlementPayFull;

    private String designPayFull;

    public String getFirstPayFull() {
        Double needPay = this.usualFee.getNeedPay();
        Double payed = this.usualFee.getPayed();

        if (needPay != null && payed != null && needPay.equals(payed)) {
            return "已付齐";
        } else {
            return "未付齐";
        }
    }

    public String getSecondPayFull() {
        Double needPay = this.usualFee.getSecondNeedPay();
        Double payed = this.usualFee.getSecondPayed();

        if (needPay != null && payed != null && needPay.equals(payed)) {
            return "已付齐";
        } else {
            return "未付齐";
        }
    }

    public String getSettlementPayFull() {
        Double needPay = this.usualFee.getSettlementNeedPay();
        Double payed = this.usualFee.getSettlementPayed();

        if (needPay != null && payed != null && needPay.equals(payed)) {
            return "已付齐";
        } else {
            return "未付齐";
        }
    }

    public String getDesignPayFull() {
        Double needPay = this.usualFee.getDesignNeedPay();
        Double payed = this.usualFee.getDesignPayed();

        if (needPay != null && payed != null && needPay.equals(payed)) {
            return "已付齐";
        } else {
            return "未付齐";
        }
    }

}
