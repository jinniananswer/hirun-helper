package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.service.IFeeItemCfgService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPaymoney;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderPaymoneyService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单费用表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Slf4j
@Service
public class OrderFeeServiceImpl extends ServiceImpl<OrderFeeMapper, OrderFee> implements IOrderFeeService {

    @Autowired
    private IOrderPaymoneyService paymoneyServiceService;

    @Autowired
    private IOrderFeeService orderFeeService;

    @Autowired
    private IFeeItemCfgService FeeItemCfgService;


    @Autowired
    private OrderFeeMapper orderFeeMapper;

    @Autowired
    private IEmployeeService employeeService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addDesignFee(OrderFeeDTO dto) {
        System.out.print("OrderFeeDTO======" + dto.toString());
        OrderFee orderFee = new OrderFee();
        OrderPaymoney OrderPaymoney = new OrderPaymoney();
        BeanUtils.copyProperties(dto, orderFee);
        //保存orderFee信息--插入设计费大类信息
        System.out.println("fee========="+dto.getCollectedFee().toString());
        orderFee.setFee(dto.getCollectedFee());
        orderFee.setActFee(dto.getCollectedFee());
        orderFee.setFeeItemId((long) 1);
        orderFee.setParentFeeItemId((long) -1);
        this.addOrderFee(orderFee);
        //处理收费类型数据
        this.addOrderPaymoney(dto,orderFee.getId());
        //保存orderFeeNew信息--插入设计费小类信息
        OrderFee orderFeeNew = new OrderFee();
        BeanUtils.copyProperties(dto, orderFeeNew);
        orderFeeNew.setFee(dto.getCollectedFee());
        orderFeeNew.setActFee(dto.getCollectedFee());
        orderFeeNew.setParentFeeItemId((long) 1);
        this.addOrderFee(orderFeeNew);


    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addDownPayment(OrderFeeDTO dto) {
        System.out.print("OrderFeeDTO======" + dto.toString());
        OrderFee orderFee = new OrderFee();
        BeanUtils.copyProperties(dto, orderFee);
        //保存orderFee信息--插入工程大类信息
        orderFee.setActFee(dto.getCollectedFee());
        orderFee.setFee(dto.getCollectedFee());
        orderFee.setFeeItemId((long) 2);
        orderFee.setParentFeeItemId((long) -1);
        orderFee.setRemark(dto.getRemark());
        orderFee.setPeriod(1);//首期工程款
        this.addOrderFee(orderFee);
        //处理收费类型数据
        this.addOrderPaymoney(dto,orderFee.getId());
    }

    @Override
    public void addOrderFee(OrderFee orderFee) {
        UserContext userContext = WebContextUtils.getUserContext();
        orderFee.setFeeEmployeeId(userContext.getEmployeeId());
        orderFee.setCreateUserId(userContext.getEmployeeId());
        orderFee.setCreateTime(LocalDateTime.now());
        orderFeeService.save(orderFee);

    }


    public void  updateOrderFee(OrderFee orderFee) {
        System.out.print("orderFee======" + orderFee.toString());
        UserContext userContext = WebContextUtils.getUserContext();
        UpdateWrapper<OrderFee> updateWrapper=new UpdateWrapper<>();
        OrderFee orderFeeUpdate = new OrderFee();
        orderFeeUpdate.setFee(orderFee.getFee());
        orderFeeUpdate.setActFee(orderFee.getActFee());
        orderFeeUpdate.setUpdateTime(LocalDateTime.now());
        orderFeeUpdate.setUpdateUserId(userContext.getEmployeeId());
        updateWrapper.eq("order_id",orderFee.getOrderId());
         this.update(orderFeeUpdate,updateWrapper);
    }

    @Override
    public void addOrderPaymoney(OrderFeeDTO dto,long id) {
        System.out.println("OrderFeeDTO========="+dto.toString());

        OrderPaymoney OrderPaymoney = new OrderPaymoney();
        BeanUtils.copyProperties(dto, OrderPaymoney);
        UserContext userContext = WebContextUtils.getUserContext();
        String cash = dto.getCash();
        String industrialBankCard = dto.getIndustrialBankCard();
        String pudongDevelopmentBankCard = dto.getPudongDevelopmentBankCard();
        String constructionBankBasic = dto.getConstructionBankBasic();
        String constructionBank3797 = dto.getConstructionBank3797();
        String ICBC3301 = dto.getGong3301();
        String ICBCInstallment = dto.getGongInstallment();
        String ABCInstallment = dto.getNongInstallment();
        System.out.println("cash=========="+cash);
        System.out.println("industrialBankCard=========="+industrialBankCard);
        System.out.println("pudongDevelopmentBankCard=========="+pudongDevelopmentBankCard);
        System.out.println("constructionBankBasic=========="+constructionBankBasic);
        System.out.println("constructionBank3797=========="+constructionBank3797);
        System.out.println("ICBC3301=========="+ICBC3301);
        System.out.println("ICBCInstallment=========="+ICBCInstallment);
        System.out.println("ABCInstallment=========="+ABCInstallment);
        OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
        OrderPaymoney.setCreateUserId(userContext.getEmployeeId());
        OrderPaymoney.setCreateTime(LocalDateTime.now());
        //处理现金收费方式
        if (StringUtils.isNotBlank(cash)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 1);
            OrderPaymoney.setFee(Integer.parseInt(cash));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理兴业刷卡收费方式
        if (StringUtils.isNotBlank(industrialBankCard)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 2);
            OrderPaymoney.setFee(Integer.parseInt(industrialBankCard));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理浦发刷卡收费方式
        if (StringUtils.isNotBlank(pudongDevelopmentBankCard)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 3);
            OrderPaymoney.setFee(Integer.parseInt(pudongDevelopmentBankCard));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理建行基本户收费方式
        if (StringUtils.isNotBlank(constructionBankBasic)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 4);
            OrderPaymoney.setFee(Integer.parseInt(constructionBankBasic));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理建行3797收费方式
        if (StringUtils.isNotBlank(constructionBank3797)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 5);
            OrderPaymoney.setFee(Integer.parseInt(constructionBank3797));
            paymoneyServiceService.save(OrderPaymoney);

        }
        //处理工行3301收费方式
        if (StringUtils.isNotBlank(ICBC3301)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 6);
            OrderPaymoney.setFee(Integer.parseInt(ICBC3301));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理工行分期收费方式
        if (StringUtils.isNotBlank(ICBCInstallment)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 7);
            OrderPaymoney.setFee(Integer.parseInt(ICBCInstallment));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理农行分期收费方式
        if (StringUtils.isNotBlank(ABCInstallment)) {
            OrderPaymoney.setFeeId(id);
            OrderPaymoney.setPaymentType((long) 8);
            OrderPaymoney.setFee(Integer.parseInt(ABCInstallment));
            paymoneyServiceService.save(OrderPaymoney);
        }

    }

    @Override
    public OrderFeeDTO loadDesignFeeInfo(Long orderId) {
        System.out.print("orderId====33333333333333======"+orderId.toString());
        List<OrderFeeDTO> orderFeeList = this.orderFeeMapper.loadDesignFeeInfo(orderId);
        System.out.print("orderFeeList=========="+orderFeeList);
        if (orderFeeList.size() <= 0) {
            return null;
        }
        OrderFeeDTO returnFeeDTO = new OrderFeeDTO();
        returnFeeDTO.setCollectedFee(orderFeeList.get(0).getActFee());
        returnFeeDTO.setOldCollectedFee(orderFeeList.get(0).getActFee());
        returnFeeDTO.setPayee(employeeService.getEmployeeNameEmployeeId(orderFeeList.get(0).getFeeEmployeeId()));
        returnFeeDTO.setFeeItemName(FeeItemCfgService.getFeeItemNameFeeItemId((long)11));
        returnFeeDTO.setParentFeeItemName(FeeItemCfgService.getFeeItemNameFeeItemId((long)1));
        returnFeeDTO.setCollectionDate(orderFeeList.get(0).getCollectionDate());
        returnFeeDTO.setRemark(orderFeeList.get(0).getRemark());
        returnFeeDTO.setOldRemark(orderFeeList.get(0).getRemark());
        for (OrderFeeDTO dto : orderFeeList) {
            if(dto.getPaymentType()==(long)1){
                returnFeeDTO.setCash(dto.getDetailFee().toString());
                returnFeeDTO.setOldCash(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)2){
                returnFeeDTO.setIndustrialBankCard(dto.getDetailFee().toString());
                returnFeeDTO.setOldIndustrialBankCard(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)3){
                returnFeeDTO.setPudongDevelopmentBankCard(dto.getDetailFee().toString());
                returnFeeDTO.setOldPudongDevelopmentBankCard(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)4){
                returnFeeDTO.setConstructionBankBasic(dto.getDetailFee().toString());
                returnFeeDTO.setOldConstructionBankBasic(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)5){
                returnFeeDTO.setConstructionBank3797(dto.getDetailFee().toString());
                returnFeeDTO.setOldConstructionBank3797(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)6){
                returnFeeDTO.setGong3301(dto.getDetailFee().toString());
                returnFeeDTO.setOldGong3301(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)7){
                returnFeeDTO.setGongInstallment(dto.getDetailFee().toString());
                returnFeeDTO.setOldGongInstallment(dto.getDetailFee().toString());
            }
            if(dto.getPaymentType()==(long)8){
                returnFeeDTO.setNongInstallment(dto.getDetailFee().toString());
                returnFeeDTO.setOldNongInstallment(dto.getDetailFee().toString());
            }

        }
        System.out.print("orderFeeList=====ddddddddddd====="+returnFeeDTO);
        return returnFeeDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void auditUpdate(OrderFeeDTO dto) {
        System.out.print("OrderFeeDTO======" + dto.toString());
        OrderFee orderFee = new OrderFee();
        BeanUtils.copyProperties(dto, orderFee);
        orderFee.setOrderId((long)7);//测试
        //设计费可以一起更新，后续多条细项的需要区分更新
        orderFee.setActFee(dto.getCollectedFee());
        orderFee.setFee(dto.getCollectedFee());
        this.updateOrderFee(orderFee);

        OrderPaymoney OrderPaymoney = new OrderPaymoney();
        BeanUtils.copyProperties(dto, OrderPaymoney);
        int collectedFee=dto.getCollectedFee();
        int oldcollectedFee=dto.getOldCollectedFee();
        String cash = dto.getCash();
        String oldCash = dto.getOldCash();
        String industrialBankCard = dto.getIndustrialBankCard();
        String oldIndustrialBankCard = dto.getOldIndustrialBankCard();
        String pudongDevelopmentBankCard = dto.getPudongDevelopmentBankCard();
        String oldPudongDevelopmentBankCard = dto.getOldPudongDevelopmentBankCard();
        String constructionBankBasic = dto.getConstructionBankBasic();
        String oldConstructionBankBasic = dto.getOldConstructionBankBasic();
        String constructionBank3797 = dto.getConstructionBank3797();
        String oldConstructionBank3797 = dto.getOldConstructionBank3797();
        String ICBC3301 = dto.getGong3301();
        String oldICBC3301 = dto.getOldGong3301();
        String ICBCInstallment = dto.getGongInstallment();
        String oldICBCInstallment = dto.getOldGongInstallment();
        String ABCInstallment = dto.getNongInstallment();
        String oldABCInstallment = dto.getOldNongInstallment();

        //如果修改了总金额，需要更改orderfee记录
        if(collectedFee!=oldcollectedFee){

        }



    }
}
