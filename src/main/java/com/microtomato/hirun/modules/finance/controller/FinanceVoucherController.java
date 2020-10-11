package com.microtomato.hirun.modules.finance.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.QueryComplainCondDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.dto.QueryVoucherAuditDTO;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 财务领款单表(FinanceVoucher)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@RestController
@RequestMapping("/api/finance/finance-voucher")
public class FinanceVoucherController {

    /**
     * 服务对象
     */
    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @PostMapping("/voucherPreparationForSupply")
    @RestResult
    public void voucherPreparationForSupply(@RequestBody List<FinanceVoucherDTO> financeVoucherDetails) {
        this.financeVoucherService.voucherPreparationForSupply(financeVoucherDetails);
    }

    @PostMapping("/voucherPreparationForConstruction")
    @RestResult
    public void voucherPreparationForConstruction(@RequestBody List<FinanceVoucherDTO> financeVoucherDetails) {
        this.financeVoucherService.voucherPreparationForConstruction(financeVoucherDetails);
    }

    @PostMapping("/voucherPreparationForOther")
    @RestResult
    public void voucherPreparationForOther(@RequestBody List<FinanceVoucherDTO> financeVoucherDetails) {
        this.financeVoucherService.voucherPreparationForOther(financeVoucherDetails);
    }

    @GetMapping("/selectDecorator")
    @RestResult
    public List<DecoratorInfoDTO> selectDecorator(DecoratorInfoDTO decoratorInfoDTO) {
        return this.financeVoucherService.selectDecorator(decoratorInfoDTO);
    }

    @GetMapping("/queryVoucherSupplyInfo")
    @RestResult
    public IPage<FinanceVoucherDTO> queryVoucherSupplyInfo(QueryVoucherAuditDTO querySupplyDTO) {
        return this.financeVoucherService.queryVoucherSupplyInfo(querySupplyDTO);
    }

    @PostMapping("/auditForSupplyPass")
    @RestResult
    public void auditForSupplyPass(@RequestBody List<FinanceVoucherDTO> financeVoucherDTO) {
        this.financeVoucherService.auditForSupplyPass(financeVoucherDTO);
    }

    @PostMapping("/auditForSupplyReject")
    @RestResult
    public void auditForSupplyReject(@RequestBody List<FinanceVoucherDTO> financeVoucherDTO) {
        this.financeVoucherService.auditForSupplyReject(financeVoucherDTO);
    }

}