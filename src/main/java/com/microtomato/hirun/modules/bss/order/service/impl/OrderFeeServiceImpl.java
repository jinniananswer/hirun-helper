package com.microtomato.hirun.modules.bss.order.service.impl;

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
        BeanUtils.copyProperties(dto, OrderPaymoney);
        //保存orderFee信息--插入设计费大类信息
        orderFee.setActFee(dto.getCollectedFee());
        orderFee.setOrderId((long) 2222);//测试
        orderFee.setFeeItemId((long) 1);
        orderFee.setParentFeeItemId((long) -1);
        orderFee.setRemark(dto.getSummary());
        this.addOrderFee(orderFee);
        //保存orderFeeNew信息--插入设计费小类信息
        OrderFee orderFeeNew = new OrderFee();
        BeanUtils.copyProperties(dto, orderFeeNew);
        orderFeeNew.setActFee(dto.getCollectedFee());
        orderFeeNew.setOrderId((long) 2222);//测试
        orderFeeNew.setParentFeeItemId((long) 1);
        orderFeeService.save(orderFeeNew);
        //需要判断使用了哪些收费方式--插入支付信息
        String cash = dto.getCash();
        String industrialBankCard = dto.getIndustrialBankCard();
        String pudongDevelopmentBankCard = dto.getPudongDevelopmentBankCard();
        String constructionBankBasic = dto.getConstructionBankBasic();
        String constructionBank3797 = dto.getConstructionBank3797();
        String ICBC3301 = dto.getICBC3301();
        String ICBCInstallment = dto.getICBCInstallment();
        String ABCInstallment = dto.getABCInstallment();
        //处理现金收费方式
        if (StringUtils.isNotBlank(cash)) {
            OrderPaymoney.setFeeId(orderFeeNew.getId());
            OrderPaymoney.setPaymentType((long) 1);
            OrderPaymoney.setFee(Integer.parseInt(cash));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理兴业刷卡收费方式
        if (StringUtils.isNotBlank(industrialBankCard)) {
            OrderPaymoney.setFeeId(orderFeeNew.getId());
            OrderPaymoney.setPaymentType((long) 2);
            OrderPaymoney.setFee(Integer.parseInt(industrialBankCard));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理浦发刷卡收费方式
        if (StringUtils.isNotBlank(pudongDevelopmentBankCard)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 3);
            OrderPaymoney.setFee(Integer.parseInt(pudongDevelopmentBankCard));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理建行基本户收费方式
        if (StringUtils.isNotBlank(constructionBankBasic)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 4);
            OrderPaymoney.setFee(Integer.parseInt(constructionBankBasic));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理建行3797收费方式
        if (StringUtils.isNotBlank(constructionBank3797)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 5);
            OrderPaymoney.setFee(Integer.parseInt(constructionBank3797));
            this.addOrderPaymoney(OrderPaymoney);

        }
        //处理工行3301收费方式
        if (StringUtils.isNotBlank(ICBC3301)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 6);
            OrderPaymoney.setFee(Integer.parseInt(ICBC3301));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理工行分期收费方式
        if (StringUtils.isNotBlank(ICBCInstallment)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 7);
            OrderPaymoney.setFee(Integer.parseInt(ICBCInstallment));
            this.addOrderPaymoney(OrderPaymoney);
        }
        //处理农行分期收费方式
        if (StringUtils.isNotBlank(ABCInstallment)) {
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long) 8);
            OrderPaymoney.setFee(Integer.parseInt(ABCInstallment));
            this.addOrderPaymoney(OrderPaymoney);
        }
    }

    @Override
    public void addOrderFee(OrderFee orderFee) {
        UserContext userContext = WebContextUtils.getUserContext();
        orderFee.setFeeEmployeeId(userContext.getEmployeeId());
        orderFee.setCreateUserId(userContext.getEmployeeId());
        orderFee.setCreateTime(LocalDateTime.now());
        orderFeeService.save(orderFee);

    }

    @Override
    public void addOrderPaymoney(OrderPaymoney orderPaymoney) {
        UserContext userContext = WebContextUtils.getUserContext();
        orderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
        orderPaymoney.setCreateUserId(userContext.getEmployeeId());
        orderPaymoney.setCreateTime(LocalDateTime.now());
        paymoneyServiceService.save(orderPaymoney);
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
        returnFeeDTO.setSummary(orderFeeList.get(0).getSummary());
        for (OrderFeeDTO dto : orderFeeList) {
            if(dto.getPaymentType()==(long)1){
                returnFeeDTO.setCash(dto.getActFee().toString());
                returnFeeDTO.setOldCash(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)2){
                returnFeeDTO.setIndustrialBankCard(dto.getActFee().toString());
                returnFeeDTO.setOldIndustrialBankCard(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)3){
                returnFeeDTO.setPudongDevelopmentBankCard(dto.getActFee().toString());
                returnFeeDTO.setOldPudongDevelopmentBankCard(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)4){
                returnFeeDTO.setConstructionBankBasic(dto.getActFee().toString());
                returnFeeDTO.setOldConstructionBankBasic(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)5){
                returnFeeDTO.setConstructionBank3797(dto.getActFee().toString());
                returnFeeDTO.setOldConstructionBank3797(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)6){
                returnFeeDTO.setICBC3301(dto.getActFee().toString());
                returnFeeDTO.setOldICBC3301(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)7){
                returnFeeDTO.setICBCInstallment(dto.getActFee().toString());
                returnFeeDTO.setOldICBCInstallment(dto.getActFee().toString());
            }
            if(dto.getPaymentType()==(long)8){
                returnFeeDTO.setABCInstallment(dto.getActFee().toString());
                returnFeeDTO.setOldABCInstallment(dto.getActFee().toString());
            }

        }
        System.out.print("orderFeeList=====ddddddddddd====="+returnFeeDTO);
        return returnFeeDTO;
    }
}
