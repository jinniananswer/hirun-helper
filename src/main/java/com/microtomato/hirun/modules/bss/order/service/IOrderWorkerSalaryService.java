package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerSalaryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorkerSalary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-01
 */
public interface IOrderWorkerSalaryService extends IService<OrderWorkerSalary> {
    /**
     * 查询工人工资
     * @param periods
     * @param orderId
     * @return
     */
    List<OrderWorkerSalaryDTO> queryOrderWorkerSalary(Integer periods,Long orderId);


    /**
     *
     * @param periods
     * @param dto
     */
    void closeWorkerSalary(Integer periods,OrderWorkerSalaryDTO dto);

    /**
     *
     * @param periods
     * @param dto
     */
    void updateWorkerSalary(Integer periods,OrderWorkerSalaryDTO dto);
}
