package com.microtomato.hirun.modules.finance.service.impl;


import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecoratorInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.DesignRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.ProjectRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherItemDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceVoucherMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ISupplyOrderService supplyOrderService;

    @Autowired
    private IDecoratorService decoratorServiceImpl;

    @Autowired
    private IStaticDataService staticDataService;



    /**
     * 材料供应制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForSupply(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            // financeVoucherDetail.setAuditStatus("1");
            Long supplyId = financeVoucherDetail.getSupplyId();
            financeVoucherDetail.setVoucherType(1L);
            SupplyOrder supplyOrder = this.supplyOrderMapper.selectById(supplyId);
            Long orderId = supplyOrder.getOrderId();
//            List<FinanceVoucherItemDTO> financeVoucherItemDTOList = new ArrayList<>();
            //  for (SupplyMaterialDTO supplyDetail : supplyDetails) {
//            FinanceVoucherItemDTO financeVoucherItemDTO = new FinanceVoucherItemDTO();
            // financeVoucherItemDTO.setFee(supplyDetail.getFee());
            financeVoucherDetail.setParentVoucherItemId("-1");
            financeVoucherDetail.setVoucherItemId("503.01");
            financeVoucherDetail.setOrderId(orderId);
//            financeVoucherItemDTOList.add(financeVoucherItemDTO);
            //   }
            //将明细更新到对应dto里
//            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 施工队制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForConstruction(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            financeVoucherDetail.setVoucherType(2L);
            FinanceVoucherItemDTO financeVoucherItemDTO = new FinanceVoucherItemDTO();
            financeVoucherDetail.setParentVoucherItemId("-1");
            financeVoucherDetail.setVoucherItemId("503.02");
            //将明细更新到对应dto里
//            financeVoucherDetail.setFinanceVoucherItemDTOList(financeVoucherItemDTOList);
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 其他制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparationForOther(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }

        financeVoucherDetails.forEach(financeVoucherDetail -> {
            financeVoucherDetail.setVoucherType(3L);
            financeVoucherDetail.setParentVoucherItemId(financeVoucherDetail.getFinanceItemId());
            financeVoucherDetail.setVoucherItemId(financeVoucherDetail.getChildFinanceItemId());
        });

        this.voucherPreparation(financeVoucherDetails);
    }

    /**
     * 制单
     *
     * @param financeVoucherDetails
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void voucherPreparation(List<FinanceVoucherDTO> financeVoucherDetails) {
        if (ArrayUtils.isEmpty(financeVoucherDetails)) {
            return;
        }
        Long voucherType = financeVoucherDetails.get(0).getVoucherType();
        Double totalMoney =0d;
            for (FinanceVoucherDTO financeVoucherDetail : financeVoucherDetails) {
                totalMoney += financeVoucherDetail.getMoney();
            }
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime createTime = RequestTimeHolder.getRequestTime();
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        Long createUserId = WebContextUtils.getUserContext().getUserId();
        //材料下单根据不同供应商需要制作不同领款单
        if (voucherType == 1L) {
            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher表数据
                FinanceVoucher financeVoucher = new FinanceVoucher();
                financeVoucher.setAuditStatus("0");
                //需要从界面传递,暂时取对应制单员
                financeVoucher.setAuditEmployeeId(employeeId);
                financeVoucher.setCreateEmployeeId(employeeId);
                financeVoucher.setCreateTime(createTime);
                financeVoucher.setOrgId(orgId);
                financeVoucher.setTotalMoney(financeVoucherDetail.getMoney());
                financeVoucher.setVoucherDate(createTime);
                financeVoucher.setStartDate(createTime);
                financeVoucher.setEndDate(TimeUtils.getForeverTime());
                financeVoucher.setVoucherEmployeeId(employeeId);
                financeVoucher.setVoucherType(financeVoucherDetail.getVoucherType());
                financeVoucher.setRemark(financeVoucherDetail.getRemark());
                financeVoucherService.save(financeVoucher);
//                List<FinanceVoucherItemDTO> financeVoucherItemDTOList = financeVoucherDetail.getFinanceVoucherItemDTOList();
                //虽然与financeVoucher一一对应，因整个制单都是一套流程，故这还是启用循环
//                for (FinanceVoucherItemDTO financeVoucherItemDTO : financeVoucherItemDTOList) {
                    FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                    //拼finance_voucher_item表数据
                    financeVoucherItem.setSupplierId(financeVoucherDetail.getSupplierId());
                    financeVoucherItem.setSupplyId(financeVoucherDetail.getSupplyId());
                    financeVoucherItem.setProjectId(financeVoucherDetail.getOrderId());
                    financeVoucherItem.setParentVoucherItemId(financeVoucherDetail.getParentVoucherItemId());
                    financeVoucherItem.setVoucherId(financeVoucher.getId());
                    financeVoucherItem.setVoucherItemId(financeVoucherDetail.getVoucherItemId());
                    financeVoucherItem.setCreateTime(createTime);
                    financeVoucherItem.setCreateUserId(createUserId);
                    financeVoucherItem.setStartDate(createTime);
                    financeVoucherItem.setFee(financeVoucherDetail.getMoney());
                    financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
                    //financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                    financeVoucherItemService.save(financeVoucherItem);

                    //制单后需要更新supply表状态
                    SupplyOrder supplyOrder = new SupplyOrder();
                    supplyOrder.setId(financeVoucherDetail.getSupplyId());
                    supplyOrder.setSupplyStatus("2");//2表示已经制单，不能重复制单
                    supplyOrderService.updateById(supplyOrder);
//                }
            });

        }
        //施工队领款单
        if (voucherType == 2L) {
            //拼finance_voucher表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setAuditStatus("0");
            //需要从界面传递,暂时取对应制单员
            financeVoucher.setAuditEmployeeId(employeeId);
            financeVoucher.setCreateEmployeeId(employeeId);
            financeVoucher.setCreateTime(createTime);
            financeVoucher.setOrgId(orgId);
            financeVoucher.setTotalMoney(totalMoney);
            financeVoucher.setVoucherDate(createTime);
            financeVoucher.setStartDate(createTime);
            financeVoucher.setEndDate(TimeUtils.getForeverTime());
            financeVoucher.setVoucherEmployeeId(employeeId);
            financeVoucher.setVoucherType(voucherType);
            financeVoucherService.save(financeVoucher);

            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher_item表数据
                FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
                financeVoucherItem.setProjectId(financeVoucherDetail.getDecoratorId());
                financeVoucherItem.setParentVoucherItemId(financeVoucherDetail.getParentVoucherItemId());
                financeVoucherItem.setVoucherId(financeVoucher.getId());
                financeVoucherItem.setVoucherItemId(financeVoucherDetail.getVoucherItemId());
                financeVoucherItem.setCreateTime(createTime);
                financeVoucherItem.setCreateUserId(createUserId);
                financeVoucherItem.setStartDate(createTime);
                financeVoucherItem.setFee(financeVoucherDetail.getMoney());
                financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
               // financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                financeVoucherItemService.save(financeVoucherItem);
            });
        }
        //其他领款单
        if (voucherType == 3L) {
            //拼finance_voucher表数据
            FinanceVoucher financeVoucher = new FinanceVoucher();
            financeVoucher.setAuditStatus("0");
            //需要从界面传递,暂时取对应制单员
            financeVoucher.setAuditEmployeeId(employeeId);
            financeVoucher.setCreateEmployeeId(employeeId);
            financeVoucher.setCreateTime(createTime);
            financeVoucher.setOrgId(orgId);
            financeVoucher.setTotalMoney(totalMoney);
            financeVoucher.setVoucherDate(createTime);
            financeVoucher.setStartDate(createTime);
            financeVoucher.setEndDate(TimeUtils.getForeverTime());
            financeVoucher.setVoucherEmployeeId(employeeId);
            financeVoucher.setVoucherType(voucherType);
            financeVoucherService.save(financeVoucher);
log.debug("financeVoucherDetails============"+financeVoucherDetails);
            financeVoucherDetails.forEach(financeVoucherDetail -> {
                //拼finance_voucher_item表数据
                FinanceVoucherItem financeVoucherItem = new FinanceVoucherItem();
               // financeVoucherItem.setProjectId(financeVoucherDetail.getDecoratorId());
                financeVoucherItem.setParentVoucherItemId(financeVoucherDetail.getParentVoucherItemId());
                financeVoucherItem.setVoucherId(financeVoucher.getId());
                financeVoucherItem.setVoucherItemId(financeVoucherDetail.getVoucherItemId());
                financeVoucherItem.setCreateTime(createTime);
                financeVoucherItem.setCreateUserId(createUserId);
                financeVoucherItem.setStartDate(createTime);
                financeVoucherItem.setFee(financeVoucherDetail.getMoney());
                financeVoucherItem.setEndDate(TimeUtils.getForeverTime());
              //  financeVoucherItem.setOrderId(financeVoucherDetail.getOrderId());
                financeVoucherItemService.save(financeVoucherItem);
            });
        }

    }

    /**
     * 查询对应工人
     * @return
     */
    @Override
    public List<DecoratorInfoDTO> selectDecorator( DecoratorInfoDTO decoratorInfoDTO) {
        List<Decorator> decorators = decoratorServiceImpl.queryAllInfo();

        List<DecoratorInfoDTO> decoratorInfoDTOArrayList = new ArrayList<>();
        for (Decorator decorator : decorators) {
            DecoratorInfoDTO decoratorInfoDTOs = new DecoratorInfoDTO();
            decoratorInfoDTOs.setName(decorator.getName());
            decoratorInfoDTOs.setDecoratorId(decorator.getDecoratorId());
            decoratorInfoDTOs.setDecoratorType(decorator.getDecoratorType());
            decoratorInfoDTOs.setDecoratorTypeName(this.staticDataService.getCodeName("DECORATOR_TYPE",decorator.getDecoratorType()));
            decoratorInfoDTOArrayList.add(decoratorInfoDTOs);
        }
log.debug("decoratorInfoDTOArrayList========="+decoratorInfoDTOArrayList);
        return decoratorInfoDTOArrayList;
    }

}