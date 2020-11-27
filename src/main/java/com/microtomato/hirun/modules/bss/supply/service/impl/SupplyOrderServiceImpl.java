package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.sequence.impl.VoucherNoCycleSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.supply.entity.dto.*;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyMaterialMapper;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderDetailMapper;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderDetailService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应订单表(SupplyOrder)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class SupplyOrderServiceImpl extends ServiceImpl<SupplyOrderMapper, SupplyOrder> implements ISupplyOrderService {

    @Autowired
    private SupplyOrderMapper supplyOrderMapper;

    @Autowired
    private SupplyOrderDetailMapper supplyOrderDetailMapper;

    @Autowired
    private ISupplyOrderService supplyOrderService;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private ISupplyOrderDetailService supplyOrderDetailService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private SupplyMaterialMapper supplyMaterialMapper;

    @Autowired
    private IDualService dualService;

    @Autowired
    private IFinanceItemService financeItemService;

    @Autowired
    private IFinanceVoucherService financeVoucherService;

    @Autowired
    private IFinanceVoucherItemService financeVoucherItemService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    /**
     * 材料下单
     *
     * @param supplyOrderInfo
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void materialOrderDeal(SupplyOrderDTO supplyOrderInfo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        LocalDateTime forever = TimeUtils.getForeverTime();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<SupplyMaterialDTO> supplyMaterialOrders = supplyOrderInfo.getSupplyMaterial();
        if (ArrayUtils.isNotEmpty(supplyMaterialOrders)) {
            //拼supplyOrder表数据
            SupplyOrder supplyOrder = new SupplyOrder();
            supplyOrder.setOrderId(supplyOrderInfo.getOrderId());
            supplyOrder.setCreateTime(RequestTimeHolder.getRequestTime());
            supplyOrder.setCreateUserId(WebContextUtils.getUserContext().getUserId());
            supplyOrder.setSupplyOrderType(supplyOrderInfo.getSupplyOrderType());
            supplyOrder.setCreateEmployeeId(employeeId);
            supplyOrder.setStartDate(now);
            supplyOrder.setEndDate(forever);
            Double totalFee = 0d;

            for (SupplyMaterialDTO supplyMaterialOrder : supplyMaterialOrders) {
                totalFee+= supplyMaterialOrder.getCostPrice() * 100 * supplyMaterialOrder.getMaterialNum();

            }
            supplyOrder.setTotalFee(totalFee.longValue());
            supplyOrderService.save(supplyOrder);
            for (SupplyMaterialDTO supplyMaterialOrder : supplyMaterialOrders) {
                SupplyOrderDetail supplyOrderDetail = new SupplyOrderDetail();
                //拼SupplyOrderDetail表数据
                supplyOrderDetail.setMaterialId(supplyMaterialOrder.getId());
                supplyOrderDetail.setSupplierId(supplyMaterialOrder.getSupplierId());
                supplyOrderDetail.setNum(supplyMaterialOrder.getMaterialNum());
                supplyOrderDetail.setSupplyId(supplyOrder.getId());
                Double fee = supplyMaterialOrder.getCostPrice() * 100;
                Double totalMoney = supplyMaterialOrder.getCostPrice() * 100 * supplyMaterialOrder.getMaterialNum();
                supplyOrderDetail.setFee(fee.longValue());
                supplyOrderDetail.setCreateEmployeeId(employeeId);
                supplyOrderDetail.setAuditStatus("0");
                supplyOrderDetail.setStartDate(now);
                supplyOrderDetail.setEndDate(forever);
                supplyOrderDetail.setTotalMoney(totalMoney.longValue());
                supplyOrderDetail.setCreateTime(RequestTimeHolder.getRequestTime());
                supplyOrderDetail.setCreateUserId(WebContextUtils.getUserContext().getUserId());
                supplyOrderDetail.setRemark(supplyMaterialOrder.getRemark());
                supplyOrderDetailService.save(supplyOrderDetail);
            }
        }

    }

    /**
     * 供应订单查询
     *
     * @param condition
     * @return
     */
    @Override
    public IPage<SupplyOrderDetailDTO> querySupplyInfo(QuerySupplyOrderDetailDTO condition) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        QueryWrapper<QuerySupplyOrderDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("b.supply_id = a.id and b.supplier_id = d.id and c.id = b.material_id and now() between a.start_date and a.end_date and now() between b.start_date and b.end_date");
        wrapper.eq("d.verify_person", employeeId);
        wrapper.eq(StringUtils.isNotBlank(condition.getAuditStatus()), "b.audit_status", condition.getAuditStatus());
        IPage<QuerySupplyOrderDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<SupplyOrderDetailDTO> pageSupplyOrders = this.supplyOrderMapper.querySupplyInfo(page, wrapper);

        List<SupplyOrderDetailDTO> supplyOrders = pageSupplyOrders.getRecords();
        supplyOrders.forEach(supplyOrder -> {
            String createUserName = this.employeeService.getEmployeeNameEmployeeId(supplyOrder.getCreateEmployeeId());
            if (supplyOrder.getAuditEmployeeId() != null) {
                String auditEmployeeName = this.employeeService.getEmployeeNameEmployeeId(supplyOrder.getAuditEmployeeId());
                supplyOrder.setAuditEmployeeName(auditEmployeeName);
            }
            supplyOrder.setCreateEmployeeName(createUserName);
            supplyOrder.setAuditStatusName(this.staticDataService.getCodeName("SUPPLY_AUDIT_STATUS", supplyOrder.getAuditStatus()));
            supplyOrder.setSupplyOrderTypeName(this.staticDataService.getCodeName("SUPPLY_ORDER_TYPE", supplyOrder.getSupplyOrderType()));
        });


        return pageSupplyOrders;
    }

    /**
     * 供应订单详情查询
     *
     * @param condition
     * @return
     */
    @Override
    public List<SupplyMaterialDTO> querySupplyDetailInfo(QuerySupplyOrderDTO condition) {

        Long supplyId = condition.getSupplyId();
        Long supplierId = condition.getSupplierId();
        List<SupplyMaterialDTO> supplyDetails = this.supplyOrderMapper.querySupplyDetailInfo(supplyId, supplierId);
        for (SupplyMaterialDTO supplyDetail : supplyDetails) {
            String supplierName = this.supplierService.querySupplierById(supplierId).getName();
            supplyDetail.setSupplierName(supplierName);
            String materialName = this.supplyMaterialMapper.selectById(supplyDetail.getMaterialId()).getName();
            supplyDetail.setName(materialName);
        }

        return supplyDetails;
    }

    /**
     * 材料制单审核不通过
     *
     * @param supplyOrderDTOS
     */
    @Override
    public void auditSupplyDetail(List<SupplyOrderDTO> supplyOrderDTOS) {
        if (ArrayUtils.isEmpty(supplyOrderDTOS)) {
            return;
        }
        supplyOrderDTOS.forEach(supplyOrderDTO -> {
            //拼supplyOrder表数据
            SupplyOrder supplyOrder = new SupplyOrder();
            supplyOrder.setId(supplyOrderDTO.getSupplyId());
            supplyOrder.setUpdateTime(RequestTimeHolder.getRequestTime());
            supplyOrder.setUpdateUserId(WebContextUtils.getUserContext().getUserId());
            supplyOrderService.updateById(supplyOrder);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitSupplyDetail(MaterialVoucherDTO data) {
        List<SupplyOrderDetailDTO> supplyOrderDetails = data.getDetails();
        if (ArrayUtils.isEmpty(supplyOrderDetails)) {
            return;
        }

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        Long totalMoney = 0L;
        String voucherNo = "QT" + this.dualService.nextval(VoucherNoCycleSeq.class);
        List<SupplyOrderDetail> details = new ArrayList<>();
        List<FinanceVoucherItem> voucherItems = new ArrayList<>();
        for (SupplyOrderDetailDTO supplyOrderDetail : supplyOrderDetails) {
            SupplyOrderDetail detail = new SupplyOrderDetail();
            detail.setId(supplyOrderDetail.getId());
            detail.setAuditStatus("1");
            detail.setAuditEmployeeId(employeeId);
            detail.setAuditComment(detail.getAuditComment());
            details.add(detail);

            Long money = new Long(Math.round(supplyOrderDetail.getTotalMoney() * 100));
            totalMoney += money;

            FinanceVoucherItem voucherItem = new FinanceVoucherItem();
            voucherItem.setProjectId(supplyOrderDetail.getId());
            voucherItem.setVoucherNo(voucherNo);
            voucherItem.setSupplierId(supplyOrderDetail.getSupplierId());
            voucherItem.setSupplyId(supplyOrderDetail.getSupplyId());
            //供应商名称
            voucherItem.setProjectType("3");
            voucherItem.setProjectName(supplyOrderDetail.getMaterialName() + "*" + supplyOrderDetail.getMaterialNum());
            voucherItem.setFee(money);
            voucherItem.setVoucherNo(voucherNo);
            voucherItem.setStartDate(now);
            voucherItem.setEndDate(TimeUtils.getForeverTime());

            String financeItemId = data.getFinanceItemId();
            voucherItem.setFinanceItemId(financeItemId);
            if (StringUtils.isNotBlank(financeItemId)) {
                FinanceItem financeItem = this.financeItemService.getByFinanceItemId(financeItemId);
                if (financeItem != null) {
                    voucherItem.setParentFinanceItemId(financeItem.getParentFinanceItemId());
                }
            }

            voucherItems.add(voucherItem);
        }


        FinanceVoucher voucher = new FinanceVoucher();
        Long orderId = supplyOrderDetails.get(0).getOrderId();
        if (orderId != null) {
            OrderBase orderBase = this.orderBaseService.getById(orderId);

            for (FinanceVoucherItem voucherItem : voucherItems) {
                voucherItem.setOrgId(orderBase.getShopId());
                voucherItem.setShopId(orderBase.getShopId());
            }
            voucher.setOrderId(orderId);
        }
        voucher.setOrderId(supplyOrderDetails.get(0).getOrderId());
        //材料制单
        voucher.setType("1");
        voucher.setItem("-1");
        voucher.setVoucherDate(data.getVoucherDate());
        voucher.setStartDate(now);
        voucher.setTotalMoney(totalMoney);
        voucher.setEndDate(TimeUtils.getForeverTime());
        voucher.setAuditStatus("0");
        voucher.setCreateEmployeeId(employeeId);
        voucher.setVoucherEmployeeId(employeeId);

        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        if (jobRole != null) {
            voucher.setOrgId(jobRole.getOrgId());
        }
        voucher.setVoucherNo(voucherNo);
        voucher.setRemark(data.getRemark());

        this.financeVoucherService.save(voucher);

        if (ArrayUtils.isNotEmpty(voucherItems)) {
            this.financeVoucherItemService.saveBatch(voucherItems);
        }

        this.supplyOrderDetailService.updateBatchById(details);
    }

}