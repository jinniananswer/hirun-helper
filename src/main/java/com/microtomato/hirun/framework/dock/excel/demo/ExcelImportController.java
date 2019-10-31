package com.microtomato.hirun.framework.dock.excel.demo;

import com.microtomato.hirun.framework.dock.excel.AbstractExcelImportController;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Steven
 * @date 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("api/demo/dock/excel")
public class ExcelImportController extends AbstractExcelImportController {

    @Autowired
    private IStevenService stevenServiceImpl;

    @PostMapping("import")
    @Transactional(rollbackFor = Exception.class)
    public String upload(@RequestParam("fileUpload") MultipartFile multipartFile) throws IOException {
        importExcel(multipartFile, UserImportData.class, new DataListener(stevenServiceImpl));
        return "success";
    }

}
