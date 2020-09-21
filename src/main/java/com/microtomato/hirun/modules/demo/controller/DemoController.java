package com.microtomato.hirun.modules.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.microtomato.hirun.modules.demo.mapper.ZhoulinMapper;
import com.microtomato.hirun.modules.demo.service.IDemoService;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.microtomato.hirun.modules.demo.service.IZhoulinService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("/api/demo/demo")
public class DemoController {

    @Autowired
    private IDemoService demoServiceImpl;

    @Autowired
    private IFuncTempService funcTempService;

    @Autowired
    private IStevenService stevenService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IZhoulinService zhoulinService;

    @Autowired
    private ZhoulinMapper zhoulinMapper;

    @GetMapping("updateEmployee/{birthday}")
    public void updateEmployee(@PathVariable("birthday") String birthday) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayLocalDate = LocalDate.parse(birthday, fmt);
        log.info("birthday: {}", birthdayLocalDate);
//        Zhoulin zhoulin = Zhoulin.builder().id(1L).name("jinnian").birthday(birthdayLocalDate).build();
//        zhoulinService.save(zhoulin);

        zhoulinMapper.insertBirthday("金念", birthdayLocalDate);

//        Employee employee = employeeService.getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getEmployeeId, 1L));
//        log.info("employee: {}", employee);
//
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate birthdayLocalDate = LocalDate.parse(birthday, fmt);
//        log.info("birthday: {}", birthdayLocalDate);
//        employee.setBirthday(birthdayLocalDate);
//        boolean b = employeeService.updateById(employee);
//        log.info("更新: {}", b);
    }

    @GetMapping("testShardTable")
    public void testShardTable() {
        stevenService.insert(
            Steven.builder()
                .createTime(LocalDateTime.now())
                .name("asiainfo")
                .id(1010L)
                .build()
        );
    }

    @GetMapping("/save")
    public void testSave() {
        demoServiceImpl.testSave();
    }

    @GetMapping("update")
    public void testUpdate() {
        demoServiceImpl.testUpdate();
    }

    @GetMapping("saveOrUpdate")
    public void testSaveOrUpdate() {
        demoServiceImpl.testSaveOrUpdate();
    }

    @GetMapping("testInsertAndSelect")
    @Transactional(rollbackFor = Exception.class)
    public void testInsertAndSelect() {
        FuncTemp funcTemp = FuncTemp.builder().funcId(1L).createTime(LocalDateTime.now()).createUserId(1L).expireDate(LocalDateTime.now().plusDays(20)).userId(1L).build();
        log.error("插入前 PO 对象: {}", funcTemp);
        funcTempService.save(funcTemp);

        log.error("插入后 PO 对象: {}", funcTemp);
        FuncTemp one = funcTempService.getOne(Wrappers.<FuncTemp>lambdaQuery().eq(FuncTemp::getId, funcTemp.getId()));

        log.error("从表里查出的 PO 对象: {}", one);
    }

    @RestResult
    @PostMapping("test1")
    public void test1() {
        log.info("===> test1");
        //return true;
    }

}
