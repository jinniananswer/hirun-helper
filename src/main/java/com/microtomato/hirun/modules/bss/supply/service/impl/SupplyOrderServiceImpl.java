package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyMaterialMapper;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderDetailMapper;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderDetailService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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


    /**
     * 材料下单
     *
     * @param supplyOrderInfo
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void materialOrderDeal(SupplyOrderDTO supplyOrderInfo) {
        List<SupplyMaterialDTO> supplyMaterialOrders = supplyOrderInfo.getSupplyMaterial();
        log.debug("supplyMaterialOrders============"+supplyMaterialOrders);
        if (ArrayUtils.isNotEmpty(supplyMaterialOrders)) {
            //拼supplyOrder表数据
            SupplyOrder supplyOrder = new SupplyOrder();
            supplyOrder.setOrderId(supplyOrderInfo.getOrderId());
            supplyOrder.setCreateTime(RequestTimeHolder.getRequestTime());
            supplyOrder.setCreateUserId(WebContextUtils.getUserContext().getUserId());
            supplyOrder.setSupplyOrderType(supplyOrderInfo.getSupplyOrderType());
            supplyOrder.setSupplyStatus("0");
            int totalFee = 0;

            for (SupplyMaterialDTO supplyMaterialOrder : supplyMaterialOrders) {
                totalFee+= supplyMaterialOrder.getCostPrice() * supplyMaterialOrder.getMaterialNum();

            }
            supplyOrder.setTotalFee(totalFee);
            supplyOrderService.save(supplyOrder);
            for (SupplyMaterialDTO supplyMaterialOrder : supplyMaterialOrders) {
                SupplyOrderDetail supplyOrderDetail = new SupplyOrderDetail();
                //拼SupplyOrderDetail表数据
                supplyOrderDetail.setMaterialId(supplyMaterialOrder.getId());
                supplyOrderDetail.setSupplierId(supplyMaterialOrder.getSupplierId());
                supplyOrderDetail.setNum(supplyMaterialOrder.getMaterialNum().toString());
                supplyOrderDetail.setSupplyId(supplyOrder.getId());
                supplyOrderDetail.setFee(supplyMaterialOrder.getCostPrice() * supplyMaterialOrder.getMaterialNum());
                supplyOrderDetail.setCreateTime(RequestTimeHolder.getRequestTime());
                supplyOrderDetail.setCreateUserId(WebContextUtils.getUserContext().getUserId());
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
    public IPage<SupplyOrderDTO> querySupplyInfo(QuerySupplyOrderDTO condition) {


        Long employeeId = condition.getEmployeeId();
        String status = "0";
        IPage<QuerySupplyOrderDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<SupplyOrderDTO> pageSupplyOrders = this.supplyOrderMapper.querySupplyInfo(page, employeeId, status);

        List<SupplyOrderDTO> supplyOrders = pageSupplyOrders.getRecords();
        supplyOrders.forEach(supplyOrder -> {
            String supplierName = this.supplierService.querySupplierById(supplyOrder.getSupplierId()).getName();
            supplyOrder.setSupplierName(supplierName);
            String createUserName = this.employeeService.queryByUserId(supplyOrder.getCreateUserId()).getName();
            supplyOrder.setCreateUserName(createUserName);
            supplyOrder.setSupplyOrderName(this.staticDataService.getCodeName("SUPPLY_ORDER_TYPE", supplyOrder.getSupplyOrderType()));
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

        Long supplyId = condition.getId();
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
            supplyOrder.setId(supplyOrderDTO.getId());
            supplyOrder.setSupplyStatus("1");//审核不通过
            supplyOrder.setUpdateTime(RequestTimeHolder.getRequestTime());
            supplyOrder.setUpdateUserId(WebContextUtils.getUserContext().getUserId());
            supplyOrderService.updateById(supplyOrder);
        });


    }

}