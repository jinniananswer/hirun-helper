package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderMaterialContractMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderMaterialContractService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import com.microtomato.hirun.modules.bss.service.entity.dto.QueryComplainCondDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (OrderMaterialContract)表服务实现类
 *
 * @author
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
@Service("orderMaterialContractService")
public class OrderMaterialContractServiceImpl extends ServiceImpl<OrderMaterialContractMapper, OrderMaterialContract> implements IOrderMaterialContractService {

    @Autowired
    private OrderMaterialContractMapper orderMaterialContractMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IPayItemCfgService payItemCfgService;

    @Autowired
    private IOrderPayItemService orderPayItemService;

    @Autowired
    private ISupplierBrandService supplierBrandService;

    @Override
    public List<OrderMaterialContractDTO> queryMaterialContracts(QueryMaterialCondDTO condDTO) {

        QueryWrapper<QueryComplainCondDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(condDTO.getCustName()), "a.cust_name", condDTO.getCustName());
        queryWrapper.eq(condDTO.getHouseId() != null, "b.houses_id", condDTO.getHouseId());
        queryWrapper.eq(condDTO.getBrandCode() != null, "c.brand_code", condDTO.getBrandCode());

        queryWrapper.apply("a.cust_id=b.cust_id and b.order_id=c.order_id and c.material_type='9' ");

        queryWrapper.apply(condDTO.getAgentEmployeeId() != null,
                " EXISTS (select 1 from order_worker d where d.order_id=b.order_id and end_date > now() " +
                        " and role_id='15' and d.employee_id=" + condDTO.getAgentEmployeeId() + ")");

        queryWrapper.apply(condDTO.getDesignEmployeeId() != null,
                " EXISTS (select 1 from order_worker e where e.order_id=b.order_id and end_date > now() " +
                        " and role_id='30' and e.employee_id=" + condDTO.getDesignEmployeeId() + ")");

        queryWrapper.apply(condDTO.getProjectEmployeeId() != null,
                " EXISTS (select 1 from order_worker e where e.order_id=b.order_id and end_date > now() " +
                        " and role_id='33' and e.employee_id=" + condDTO.getProjectEmployeeId() + ")");

        queryWrapper.apply(condDTO.getMainMaterialEmployeeId() != null,
                " EXISTS (select 1 from order_worker e where e.order_id=b.order_id and end_date > now() " +
                        " and role_id='46' and e.employee_id=" + condDTO.getMainMaterialEmployeeId() + ")");

        List<OrderMaterialContractDTO> list = this.baseMapper.queryMaterialContracts(queryWrapper);
        if (ArrayUtils.isEmpty(list)) {
            return null;
        }
        for (OrderMaterialContractDTO dto : list) {
            dto.setAgentName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getAgentName());
            dto.setDesignName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getDesignerName());
            dto.setProjectManageName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getProjectManagerName());
            dto.setMainMaterialName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getMaterialName());
            dto.setCabinetDesignerName(orderDomainService.getUsualOrderWorker(dto.getOrderId()).getCabinetDesignerName());
            dto.setHouseName(housesService.queryHouseName(dto.getHouseId()));
            dto.setShopName(orgService.queryByOrgId(dto.getShopId()).getName());
            dto.setKindName(staticDataService.getCodeName("BRAND_TYPE",dto.getKindId()));
            String brandCode=dto.getBrandCode();
            String mainType=dto.getMaterialType();
            dto.setFeeTypeName(payItemCfgService.getPayItem(Long.parseLong(mainType)).getName());
            dto.setBrandCodeName(supplierBrandService.queryByPayItem(Long.parseLong(brandCode)).getName());
            dto.setActualFee(dto.getActualFee() / 100);
            dto.setContractFee(dto.getContractFee() / 100);
            dto.setDiscountFee(dto.getDiscountFee() / 100);
        }
        return list;
    }

    @Override
    public void updateContractInfoByPay(Long payNo) {

        List<OrderPayItem> payItems=orderPayItemService.queryByPayNo(payNo);
        LocalDateTime now = RequestTimeHolder.getRequestTime();


        if (ArrayUtils.isNotEmpty(payItems)) {
            for (OrderPayItem payItem : payItems) {
                if(payItem.getOrderId()==null){
                    continue;
                }
                //获取配置
                Long payItemId = new Long(payItem.getPayItemId());
                PayItemCfg payItemCfg = payItemCfgService.getPayItem(payItemId);
                if (payItemCfg == null) {
                    continue;
                }
                //不是主材款跳出本次循环
                if (!payItemCfg.getParentPayItemId().equals(9L)) {
                    continue;
                }
                //查看是否曾经有过相同品牌的数据
                OrderMaterialContract historyContract = this.queryInfoByOrderIdAndBrand(payItem.getOrderId(), "9", payItemCfg.getId() + "");
                SupplierBrand supplierBrand=supplierBrandService.queryByPayItem(payItemId);

                if (historyContract == null) {
                    OrderMaterialContract orderMaterialContract = new OrderMaterialContract();
                    orderMaterialContract.setOrderId(payItem.getOrderId());
                    orderMaterialContract.setMaterialType(payItemCfg.getParentPayItemId() + "");
                    orderMaterialContract.setBrandCode(payItemCfg.getId()+"");
                    orderMaterialContract.setActualFee(payItem.getFee());
                    orderMaterialContract.setStartDate(now);
                    orderMaterialContract.setEndDate(TimeUtils.getForeverTime());
                    orderMaterialContract.setContractFee(0L);
                    orderMaterialContract.setDiscountFee(0L);
                    orderMaterialContract.setPayTime(payItem.getCreateTime());
                    if(supplierBrand!=null){
                        orderMaterialContract.setKindId(supplierBrand.getBrandType());
                    }
                    this.baseMapper.insert(orderMaterialContract);
                } else {
                    Long actualFee=historyContract.getActualFee();
                    Long newActualFee=actualFee+payItem.getFee();
                    historyContract.setActualFee(newActualFee);
                    this.baseMapper.updateById(historyContract);
                }
            }
        }
    }

    @Override
    public List<OrderMaterialFeeDetailDTO> getDetail(Long orderId) {
        List<OrderPayItem> orderPayItems=orderPayItemService.queryByOrderId(orderId);

        List<OrderMaterialFeeDetailDTO> result=new ArrayList<>();
        if(ArrayUtils.isEmpty(orderPayItems)){
            return result;
        }
        for(OrderPayItem payItem:orderPayItems){

            if(!payItem.getParentPayItemId().equals(9L)){
                continue;
            }
            OrderMaterialFeeDetailDTO dto=new OrderMaterialFeeDetailDTO();
            dto.setPayTime(payItem.getCreateTime());
            dto.setPeriodsName("全款");
            dto.setFeeTypeName(payItemCfgService.getPayItem(payItem.getParentPayItemId()).getName());
            dto.setBrandCodeName(payItemCfgService.getPayItem(payItem.getPayItemId()).getName());
            dto.setFee(payItem.getFee()/100);
            dto.setAgentName(orderDomainService.getUsualOrderWorker(payItem.getOrderId()).getAgentName());
            dto.setDesignName(orderDomainService.getUsualOrderWorker(payItem.getOrderId()).getDesignerName());
            dto.setCabinetDesignerName(orderDomainService.getUsualOrderWorker(payItem.getOrderId()).getCabinetDesignerName());
            dto.setRemark(payItem.getRemark());
            result.add(dto);
        }
        return result;
    }

    private OrderMaterialContract queryInfoByOrderIdAndBrand(Long orderId, String type, String brandCode) {
        OrderMaterialContract orderMaterialContract = this.baseMapper.selectOne(new QueryWrapper<OrderMaterialContract>().lambda()
                .eq(OrderMaterialContract::getOrderId, orderId)
                .eq(OrderMaterialContract::getMaterialType, type)
                .eq(OrderMaterialContract::getBrandCode, brandCode));
        return orderMaterialContract;
    }
}