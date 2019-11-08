package com.microtomato.hirun.framework.harbour.excel.demo;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.harbour.excel.AbstractExcelHarbour;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("api/demo/harbour/excel")
public class ExcelDemoHarbourController extends AbstractExcelHarbour {

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private IStevenService stevenServiceImpl;

    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userServiceImpl.list();
        List<UserExportData> datas = new ArrayList<>(list.size());
        for (User user : list) {
            UserExportData data = new UserExportData();
            BeanUtils.copyProperties(user, data);
            datas.add(data);
        }

        // 调用基类导出函数
        exportExcel(response, "users", UserExportData.class, datas, ExcelTypeEnum.XLSX);
    }

    @PostMapping("import")
    @RestResult
    public void upload(@RequestParam("fileUpload") MultipartFile multipartFile) throws IOException {

        // 调用基类导入函数
        importExcel(multipartFile, UserImportData.class, new UserReadListener(stevenServiceImpl));

    }

}
