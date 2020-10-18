package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderWholeRoomDrawService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiaocl
 * @date ：Created in 2020/6/26 16:25
 * @description：图纸审核
 * @modified By：
 * @version: 1$
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-drawingaudit")
public class OrderDrawingauditController {

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IOrderWholeRoomDrawService iOrderWholeRoomDrawService;

    @PostMapping("/submitDrawingauditInfo")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void save(@RequestBody OrderWholeRoomDrawDTO dto) {
        iOrderWholeRoomDrawService.saveAuditWholeRoom(dto);
    }
}
