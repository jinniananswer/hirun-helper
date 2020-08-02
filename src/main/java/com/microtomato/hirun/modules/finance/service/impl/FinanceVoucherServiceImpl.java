package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.salary.entity.dto.DesignRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.ProjectRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherItemDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务领款单表(FinanceVoucher)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Service
@Slf4j
public class FinanceVoucherServiceImpl extends ServiceImpl<FinanceVoucherMapper, FinanceVoucher> implements IFinanceVoucherService {

    @Autowired
    private FinanceVoucherMapper financeVoucherMapper;

    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @Autowired
    private IFinanceVoucherItemService financeVoucherItemService;

    @Autowired
    private SupplyOrderMapper supplyOrderMapper;



    /**
     * 材料供应制单
     *
     * @param financeVoucherDetails
     */
    @Override
    public void voucherPreparationForSupply(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            // financeVoucherDetail.setAuditStatus("1");
            Long supplyId = financeVoucherDetail.getId();
            Long supplierId = financeVoucherDetail.getSupplierId();
            financeVoucherDetail.setVoucherType(1L);
            SupplyOrder supplyOrder = this.supplyOrderMapper.selectById(supplyId);
            Long orderId =supplyOrder.getOrderId();
            List<FinanceVoucherItemDTO> financeVoucherItemDTOList=new ArrayList<>();
          //  for (SupplyMaterialDTO supplyDetail : supplyDetails) {
                FinanceVoucherItemDTO financeVoucherItemDTO= new FinanceVoucherItemDTO();
               // financeVoucherItemDTO.setFee(supplyDetail.getFee());
                financeVoucherItemDTO.setParentVoucherItemId("-1");
                financeVoucherItemDTO.setVoucherItemId("503.01");
                financeVoucherItemDTO.setOrderId(orderId);
                financeVoucherItemDTO.setSupplierId(supplierId);
                financeVoucherItemDTO.setSupplyId(supplyId);
                financeVoucherItemDTOList.add(financeVoucherItemDTO);
         //   }
            //将明细更新到对应dto里
            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 施工队制单
     *
     * @param financeVoucherDetails
     */
    @Override
    public void voucherPreparationForConstruction(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
             financeVoucherDetail.setVoucherType(2L);
             List<FinanceVoucherItemDTO> financeVoucherItemDTOList=new ArrayList<>();
            FinanceVoucherItemDTO financeVoucherItemDTO= new FinanceVoucherItemDTO();
             financeVoucherItemDTO.setParentVoucherItemId("-1");
            financeVoucherItemDTO.setFee(financeVoucherDetail.getMoney());
            financeVoucherItemDTO.setVoucherItemId("503.02");
            financeVoucherItemDTOList.add(financeVoucherItemDTO);
            //将明细更新到对应dto里
            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 制单
     *
     * @param financeVoucherDetails
     */
    @Override
    public void voucherPreparation(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }
        int totalFee = 0;
        for (FinanceVoucherDTO financeVoucherDetail : financeVoucherDetails) {
            totalFee += financeVoucherDetail.getMoney();
        }
        Long employeeId =WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime createTime =RequestTimeHolder.getRequestTime();
        Long orgId =WebContextUtils.getUserContext().getOrgId();
        Long createUserId =WebContextUtils.getUserContext().getUserId();
        Long voucherType =financeVoucherDetails.get(0).getVoucherType();
        //材料下单需要多笔领款单
        if(voucherType==1L){
            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher表数据
                FinanceVoucher financeVoucher = new FinanceVoucher();
                financeVoucher.setAuditStatus("0");
                //需要从界面传递,暂时取对应制单员
                financeVoucher.setAuditEmployeeId(employeeId);
                financeVoucher.setCreateEmployeeId(employeeId);
                financeVoucher.setCreateTime(createTime);
                financeVoucher.setOrgId(orgId);
                financeVoucher.setTotalMoney(financeVoucherDetail.getTotalMoney());
                financeVoucher.setVoucherType(financeVoucherDetail.getVoucherType());
                financeVoucherService.save(financeVoucher);
                List<FinanceVoucherItemDTO> financeVoucherItemDTOList=financeVoucherDetail.getFinanceVoucherItemDTOList();
                for (FinanceVoucherItemDTO financeVoucherItemDTO : financeVoucherItemDTOList) {
                    FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                    //拼finance_voucher_item表数据
                    if("1".equals(financeVoucherDetail.getVoucherType())){
                        financeVoucherItem.setSupplierId(financeVoucherItemDTO.getSupplierId());
                        financeVoucherItem.setSupplyId(financeVoucherItemDTO.getSupplyId());
                    }
                    financeVoucherItem.setProjectId(financeVoucherItemDTO.getProjectId());
                    financeVoucherItem.setParentVoucherItemId(financeVoucherItemDTO.getParentVoucherItemId());
                    financeVoucherItem.setVoucherId(financeVoucher.getId());
                    financeVoucherItem.setVoucherItemId(financeVoucherItemDTO.getVoucherItemId());
                    financeVoucherItem.setCreateTime(createTime);
                    financeVoucherItem.setCreateUserId(createUserId);
                    financeVoucherItemService.save(financeVoucherItem);
                }
            });
        }

    }

}