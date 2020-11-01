package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.service.ICollectionItemCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.house.entity.po.Houses;
import com.microtomato.hirun.modules.bss.house.service.IHousesService;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.NormalPayNoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayItem;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.mapper.NormalPayNoMapper;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IDecoratorService;
import com.microtomato.hirun.modules.bss.order.service.INormalPayItemService;
import com.microtomato.hirun.modules.bss.order.service.INormalPayNoService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import com.microtomato.hirun.modules.finance.entity.dto.ReceiveReceiptDTO;
import com.microtomato.hirun.modules.finance.service.IFinanceFieldService;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-11-02 00:59
 **/
@Service
public class FinanceFieldServiceImpl implements IFinanceFieldService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private OrderBaseMapper orderBaseMapper;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private IHousesService housesService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private NormalPayNoMapper normalPayNoMapper;

    @Autowired
    private INormalPayItemService normalPayItemService;

    @Autowired
    private ICollectionItemCfgService collectionItemCfgService;

    @Autowired
    private ISupplierBrandService supplierBrandService;

    @Autowired
    private IDecoratorService decoratorService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IOrderPayNoService orderPayNoService;

    @Autowired
    private INormalPayNoService normalPayNoService;

    @Override
    public IPage<FinanceOrderTaskDTO> queryBusinessReceipt(FinanceOrderTaskQueryDTO condition) {
        QueryWrapper<FinanceOrderTaskQueryDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("b.cust_id = a.cust_id ");
        wrapper.apply("c.order_id = a.order_id");
        wrapper.like(StringUtils.isNotBlank(condition.getCustName()), "b.cust_name", condition.getCustName());
        wrapper.eq(StringUtils.isNotBlank(condition.getAuditStatus()), "c.audit_status", condition.getAuditStatus());
        wrapper.eq(condition.getHousesId() != null, "a.houses_id", condition.getHousesId());
        wrapper.eq(StringUtils.isNotBlank(condition.getMobileNo()), "b.mobile_no", condition.getMobileNo());
        wrapper.eq(StringUtils.isNotBlank(condition.getReceiptNo()), "c.receipt_no", condition.getReceiptNo());

        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        wrapper.eq("c.finance_employee_id", employeeId);
        wrapper.orderByAsc("a.status", "a.create_time");

        IPage<FinanceOrderTaskQueryDTO> page = new Page<>(condition.getPage(), condition.getLimit());
        IPage<FinanceOrderTaskDTO> pageTasks = this.orderBaseMapper.queryFinanceOrderTaskInConsole(page, wrapper);

        List<FinanceOrderTaskDTO> tasks = pageTasks.getRecords();
        if (ArrayUtils.isEmpty(tasks)) {
            return pageTasks;
        }

        tasks.forEach(task -> {
            task.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_MODE", task.getHouseLayout()));
            task.setTypeName(this.staticDataService.getCodeName("ORDER_TYPE", task.getType()));
            task.setAuditStatusName(this.staticDataService.getCodeName("PAY_AUDIT_STATUS", task.getAuditStatus()));
            OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByTypeStatus(task.getType(), task.getStatus());
            if (statusCfg != null) {
                task.setStatusName(statusCfg.getStatusName());
            }

            if (task.getHousesId() != null) {
                Houses house = this.housesService.getHouse(task.getHousesId());
                if (house != null) {
                    task.setHousesName(house.getName());
                }
            }

            if (task.getFinanceEmployeeId() != null) {
                String financeEmployeeName = this.employeeService.getEmployeeNameEmployeeId(task.getFinanceEmployeeId());
                task.setFinanceEmployeeName(financeEmployeeName);
            }
        });
        return pageTasks;
    }

    @Override
    public IPage<NormalPayNoDTO> queryNonBusinessReceipt(NonCollectFeeQueryDTO queryCondition) {
        IPage<NonCollectFeeQueryDTO> request = new Page<>(queryCondition.getPage(), queryCondition.getLimit());
        String[] feeTime = queryCondition.getFeeTime();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        QueryWrapper wrapper = new QueryWrapper();

        wrapper.eq("pay_employee_id", employeeId);
        wrapper.ge("end_date", RequestTimeHolder.getRequestTime());
        wrapper.eq(StringUtils.isNotBlank(queryCondition.getReceiptNo()), "receipt_no", queryCondition.getReceiptNo());
        wrapper.eq(StringUtils.isNotBlank(queryCondition.getAuditStatus()), "audit_status", queryCondition.getAuditStatus());

        if (ArrayUtils.isNotEmpty(feeTime)) {
            wrapper.between(ArrayUtils.isNotEmpty(feeTime), "pay_date", feeTime[0], feeTime[1]);
        }
        if (ArrayUtils.isNotEmpty(queryCondition.getPayItemId())) {
            String payItemIds = "";
            List<String> tempPayItemIds = queryCondition.getPayItemId();
            for (String tempPayItemId : tempPayItemIds) {
                payItemIds += tempPayItemId.split("_")[1] + ",";
            }

            wrapper.exists("select 1 from normal_pay_item b where b.pay_item_id in (" + payItemIds.substring(0, payItemIds.length() - 1)+ ") and b.pay_no = a.pay_no ");
        }
        IPage<NormalPayNo> infos = this.normalPayNoMapper.queryPayNoInfo(request, wrapper);
        List<NormalPayNo> normalPayNos = infos.getRecords();
        List<NormalPayNoDTO> normalPayInfos = new ArrayList<>();
        for (NormalPayNo normalPayNo : normalPayNos) {
            NormalPayNoDTO normalPayInfo = new NormalPayNoDTO();
            normalPayInfo.setPayDate(normalPayNo.getPayDate());
            normalPayInfo.setPayNo(normalPayNo.getPayNo());
            normalPayInfo.setAuditStatus(normalPayNo.getAuditStatus());

            if (employeeId != null) {
                EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
                normalPayInfo.setEmployeeName(employeeDO.getEmployee().getName());
            }

            Long financeEmployeeId = normalPayNo.getFinanceEmployeeId();
            if (financeEmployeeId != null) {
                EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
                normalPayInfo.setFinanceEmployeeName(employeeDO.getEmployee().getName());
            }
            normalPayInfo.setAuditStatusName(this.staticDataService.getCodeName("PAY_AUDIT_STATUS", normalPayNo.getAuditStatus()));
            if (normalPayNo.getTotalMoney() != null) {
                normalPayInfo.setTotalMoney(normalPayNo.getTotalMoney().doubleValue() / 100);
            } else {
                normalPayInfo.setTotalMoney(0d);
            }

            List<NormalPayItem> payItems = this.normalPayItemService.queryByPayNo(normalPayNo.getPayNo());
            if (ArrayUtils.isNotEmpty(payItems)) {
                StringBuilder sb = new StringBuilder();
                payItems.forEach(payItem -> {
                    String payItemName = this.getPayItemName(payItem);
                    if (StringUtils.isNotBlank(payItemName)) {
                        sb.append(payItemName + "|");
                    }
                });
                if (sb.length() > 0) {
                    normalPayInfo.setPayItemName(sb.substring(0, sb.length() -1));
                }
            }
            normalPayInfos.add(normalPayInfo);
        }
        IPage<NormalPayNoDTO> result = new Page<>();
        result.setSize(infos.getSize());
        result.setTotal(infos.getTotal());
        result.setPages(infos.getPages());
        result.setCurrent(infos.getCurrent());
        result.setRecords(normalPayInfos);
        return result;
    }

    /**
     * 获取付款项名称
     * @param payItem
     * @return
     */
    private String getPayItemName(NormalPayItem payItem) {
        Long projectId = payItem.getProject();
        CollectionItemCfg collectionItemCfg = this.collectionItemCfgService.getFeeItem(payItem.getPayItemId());
        String payItemName = collectionItemCfg.getName();

        String parentPayItemName = null;
        if (payItem.getParentPayItemId() != null && !payItem.getParentPayItemId().equals(-1L)) {
            parentPayItemName = this.collectionItemCfgService.getFeeItem(payItem.getParentPayItemId()).getName();
        }

        String extend = collectionItemCfg.getExtend();
        String projectName = "";
        //根据不同的收费小类信息区分项目信息
        //查询品牌信息
        if (StringUtils.equals("B", extend)) {
            SupplierBrand supplierBrands = this.supplierBrandService.getSupplierBrand(projectId);
            projectName = supplierBrands.getName();
        } else if (StringUtils.equals("D", extend)) {
            //查询工人信息
            Decorator decorators = this.decoratorService.getDecorator(projectId);
            projectName = decorators.getName();
        } else if (StringUtils.equals("E", extend)) {
            //查询员工信息
            Employee employees = this.employeeService.queryByUserId(projectId);
            projectName = employees.getName();
        } else if (StringUtils.equals("S", extend) || StringUtils.equals("C", extend)) {
            //查询门店信息
            Org orgs = this.orgService.queryByOrgId(projectId);
            projectName = orgs.getName();
        }

        if (StringUtils.isNotBlank(parentPayItemName)) {
            payItemName = parentPayItemName + "-" + payItemName;
        }

        if (StringUtils.isNotBlank(projectName)) {
            payItemName = payItemName + "-" + projectName;
        }

        return payItemName;
    }

    @Override
    public void submitBusinessReceiveReceipt(ReceiveReceiptDTO data) {
        OrderPayNo payNoData = new OrderPayNo();
        payNoData.setOrderId(data.getOrderId());
        payNoData.setPayNo(data.getPayNo());
        payNoData.setAuditStatus(data.getAuditStatus());
        payNoData.setReceiveComment(data.getReceiveComment());

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        this.orderPayNoService.update(payNoData, Wrappers.<OrderPayNo>lambdaQuery().eq(OrderPayNo::getOrderId, data.getOrderId()).eq(OrderPayNo::getPayNo, data.getPayNo()).ge(OrderPayNo::getEndDate, now));
    }

    @Override
    public void submitNonBusinessReceiveReceipt(ReceiveReceiptDTO data) {
        NormalPayNo payNoData = new NormalPayNo();
        payNoData.setPayNo(data.getPayNo());
        payNoData.setAuditStatus(data.getAuditStatus());
        payNoData.setReceiveComment(data.getReceiveComment());

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        this.normalPayNoService.update(payNoData, Wrappers.<NormalPayNo>lambdaQuery().eq(NormalPayNo::getPayNo, data.getPayNo()).ge(NormalPayNo::getEndDate, now));
    }
}
