package com.microtomato.hirun.framework.dock.excel.demo;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.microtomato.hirun.framework.dock.excel.AbstractExcelExportController;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("api/demo/dock/excel")
public class ExcelExportController extends AbstractExcelExportController {

    @Autowired
    private IUserService userServiceImpl;

    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userServiceImpl.list();
        List<UserExportData> xx = new ArrayList<>(list.size());
        for (User user : list) {
            UserExportData data = new UserExportData();
            BeanUtils.copyProperties(user, data);
            xx.add(data);
        }
        exportExcel(response, "users", UserExportData.class, xx, ExcelTypeEnum.XLS);
    }

}
