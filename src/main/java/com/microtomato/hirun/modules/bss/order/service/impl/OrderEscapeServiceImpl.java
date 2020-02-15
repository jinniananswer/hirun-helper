package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderEscapeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderDetailDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderEscape;
import com.microtomato.hirun.modules.bss.order.mapper.OrderEscapeMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderEscapeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
@Slf4j
@Service
public class OrderEscapeServiceImpl extends ServiceImpl<OrderEscapeMapper, OrderEscape> implements IOrderEscapeService {

    @Autowired
    private IOrderDomainService domainService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrderWorkerService workerService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Override
    public void saveOrderEscape(OrderEscape orderEscape) {
        if(orderEscape.getId()==null){
            //设置状态为未审核
            orderEscape.setEscapeStatus("1");
            this.baseMapper.insert(orderEscape);
        }else{
            this.baseMapper.updateById(orderEscape);
        }

    }

    @Override
    public OrderEscapeDTO getEscapeInfo(Long orderId) {
        OrderEscape orderEscape = this.baseMapper.selectOne(new QueryWrapper<OrderEscape>().lambda()
                .eq(OrderEscape::getOrderId, orderId).eq(OrderEscape::getEscapeStatus, "1"));
        OrderEscapeDTO dto = new OrderEscapeDTO();

        if (orderEscape == null) {
            OrderDetailDTO infoDTO = domainService.getOrderDetail(orderId);
            dto.setEscapeNode(infoDTO.getStatus());
            dto.setEscapeNodeName(infoDTO.getStatusName());
        } else {
            BeanUtils.copyProperties(orderEscape, dto);
            dto.setEscapeNodeName(staticDataService.getCodeName("ORDER_STATUS", dto.getEscapeNode()));
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitDirectorAudit(OrderEscape orderEscape) {
        this.saveOrderEscape(orderEscape);
        UserContext userContext= WebContextUtils.getUserContext();
        //主管走NEXT_DIRECTOR，组长走NEXT_HEADMAN
        domainService.orderStatusTrans(orderEscape.getOrderId(),"NEXT_DIRECTOR");
        //todo 怎么将单子流转到选中的主管去，因为客户代表与设计师还有其他的角色对应的上级角色不一样
       EmployeeInfoDTO infoDTO= employeeService.queryEmployeeInfoByEmployeeId(userContext.getEmployeeId());
       Long parentEmployeeId=infoDTO.getParentEmployeeId();
       //19客户代表用作测试
        workerService.updateOrderWorker(orderEscape.getOrderId(),48L,1473L);

    }

    @Override
    public void submitBack(OrderEscape orderEscape) {
        //如果id不为空，则说明已保存过跑单数据，应将跑单数据改成失效，一个客户有效的跑单数据只有一条
        if(orderEscape.getId()!=null){
            orderEscape.setEscapeStatus("2");
            this.baseMapper.updateById(orderEscape);
        }
        //跑单返回到上一个步骤需要从订单表中取上一个步骤的数据，不能单纯的通过orderStatusTrans方法，因为可以从很多环节做跑单
        OrderBase orderBase=orderBaseService.queryByOrderId(orderEscape.getOrderId());
        Integer previousStage=orderBase.getPreviousStage();
        String previousStatus=orderBase.getPreviousStatus();
        orderBase.setStage(previousStage);
        orderBase.setStatus(previousStatus);
        orderBase.setPreviousStage(100);
        orderBase.setPreviousStatus("97");
        orderBaseService.updateOrderBase(orderBase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void closeOrder(OrderEscape orderEscape) {
        orderEscape.setEscapeStatus("2");
        this.baseMapper.updateById(orderEscape);
        domainService.orderStatusTrans(orderEscape.getOrderId(),"NEXT");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void auditBack(OrderEscape orderEscape) {
        this.baseMapper.updateById(orderEscape);
        domainService.orderStatusTrans(orderEscape.getOrderId(),"AUDIT_NO");
    }


}
