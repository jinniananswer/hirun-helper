package com.microtomato.hirun.modules.bss.order.service;

/**
 * @author ：xiaocl
 * @date ：Created in 2020/6/17 19:21
 * @description：设计师模块公用
 * @modified By：
 * @version: 1$
 */
public interface IDesignerCommonService {

    void dealOrderWorkerAction(String action,Object obj);

    Long getWorkerId(Long orderId,Long roleId,Long employeeId);
}
