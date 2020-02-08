package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPaymoney;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderPaymoneyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addOrderFee(OrderFeeDTO dto) {
        UserContext userContext= WebContextUtils.getUserContext();
        OrderFee orderFee=new OrderFee();
        OrderPaymoney OrderPaymoney=new OrderPaymoney();
        BeanUtils.copyProperties(dto, orderFee);
        BeanUtils.copyProperties(dto, OrderPaymoney);
        //保存orderFee信息--插入设计费大类信息
        orderFee.setActFee(dto.getCollectedFee());
        orderFee.setCreateTime(LocalDateTime.now());
        orderFee.setOrderId((long)1111);//测试
        orderFee.setFeeEmployeeId(userContext.getEmployeeId());
        orderFeeService.save(orderFee);

        OrderFee orderFeeNew=new OrderFee();
        BeanUtils.copyProperties(dto, orderFeeNew);
        //保存orderFeeNew信息--插入设计费小类信息
        orderFeeNew.setActFee(dto.getCollectedFee());
        orderFeeNew.setCreateTime(LocalDateTime.now());
        orderFeeNew.setOrderId((long)1111);//测试
        orderFeeNew.setFeeEmployeeId((long)1);
        orderFeeService.save(orderFeeNew);
        //需要判断使用了哪些收费方式--插入支付信息
        String cash = dto.getCash();
        String industrialBankCard = dto.getIndustrialBankCard();
        String pudongDevelopmentBankCard= dto.getPudongDevelopmentBankCard();
        String constructionBankBasic= dto.getConstructionBankBasic();
        String constructionBank3797=dto.getConstructionBank3797();
        String ICBC3301= dto.getICBC3301();
        String ICBCInstallment=dto.getICBCInstallment();
        String ABCInstallment=dto.getABCInstallment();
        //处理现金收费方式
        if (StringUtils.isNotBlank(cash)){
            OrderPaymoney.setFeeId(orderFeeNew.getId());
            OrderPaymoney.setPaymentType((long)1);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(cash));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理兴业刷卡收费方式
        if (StringUtils.isNotBlank(industrialBankCard)){
            OrderPaymoney.setFeeId(orderFeeNew.getId());
            OrderPaymoney.setPaymentType((long)2);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(industrialBankCard));
            paymoneyServiceService.save(OrderPaymoney);
        }

        //处理浦发刷卡收费方式
        if (StringUtils.isNotBlank(pudongDevelopmentBankCard)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)3);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(pudongDevelopmentBankCard));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理建行基本户收费方式
        if (StringUtils.isNotBlank(constructionBankBasic)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)4);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(constructionBankBasic));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理建行3797收费方式
        if (StringUtils.isNotBlank(constructionBank3797)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)5);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(constructionBank3797));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理工行3301收费方式
        if (StringUtils.isNotBlank(ICBC3301)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)6);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(ICBC3301));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理工行分期收费方式
        if (StringUtils.isNotBlank(ICBCInstallment)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)7);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(ICBCInstallment));
            paymoneyServiceService.save(OrderPaymoney);
        }
        //处理农行分期收费方式
        if (StringUtils.isNotBlank(ABCInstallment)){
            OrderPaymoney.setFeeId(orderFee.getId());
            OrderPaymoney.setPaymentType((long)8);
            OrderPaymoney.setFeeEmployeeId(userContext.getEmployeeId());
            OrderPaymoney.setFee(Integer.parseInt(ABCInstallment));
            paymoneyServiceService.save(OrderPaymoney);
        }
    }

//    @Override
//    public List<CustPreparationDTO> loadFeeDetail(String type) {
//        List<CustPreparationDTO> list=this.preparationMapper.loadPreparationHistory(mobileNo);
//        if(list.size()<=0){
//            return null;
//        }
//        for(CustPreparationDTO dto:list){
//            dto.setPrepareEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getPrepareEmployeeId()));
//            dto.setEnterEmployeeName(employeeService.getEmployeeNameEmployeeId(dto.getEnterEmployeeId()));
//            dto.setPreparationStatusName(staticDataService.getCodeName("PREPARATION_STATUS",dto.getStatus()+""));
//        }
//        return list;
//    }
}
