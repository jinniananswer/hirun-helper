package com.microtomato.hirun.modules.bss.salary.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.CreateEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.QueryEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRedebitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 员工补扣款信息表(SalaryRedebit)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-30 20:34:51
 */
@RestController
@RequestMapping("/api/bss.salary/salary-redebit")
public class SalaryRedebitController {

    /**
     * 服务对象
     */
    @Autowired
    private ISalaryRedebitService salaryRedebitService;

    /**
     * 界面查询员工某月工资数据，如果该月还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @RequestMapping("/queryRedebits")
    @RestResult
    public IPage<EmployeeRedebitDTO> queryRedebits(@RequestBody QueryEmployeeRedebitDTO param) {
        return this.salaryRedebitService.queryEmployeeRedebits(param);
    }

    /**
     * 创建补扣款信息
     * @param param
     */
    @RequestMapping("/createRedebit")
    @RestResult
    public void createRedebit(@RequestBody CreateEmployeeRedebitDTO param) {
        this.salaryRedebitService.createRedebit(param);
    }

    /**
     * 删除未审核的补扣款信息
     * @param redebits
     */
    @RequestMapping("/deleteRedebits")
    @RestResult
    public void deleteRedebits(@RequestBody List<EmployeeRedebitDTO> redebits) {
        this.salaryRedebitService.deleteRedebits(redebits);
    }

    /**
     * 审核通过补扣款信息
     * @param redebits
     */
    @RequestMapping("/auditPass")
    @RestResult
    public void auditPass(@RequestBody List<EmployeeRedebitDTO> redebits) {
        this.salaryRedebitService.auditRedebit(redebits, true);
    }

    /**
     * 审核不通过补扣款信息
     * @param redebits
     */
    @RequestMapping("/auditNo")
    @RestResult
    public void auditNo(@RequestBody List<EmployeeRedebitDTO> redebits) {
        this.salaryRedebitService.auditRedebit(redebits, false);
    }
}