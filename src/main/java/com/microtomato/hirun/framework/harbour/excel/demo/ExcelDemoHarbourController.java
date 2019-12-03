package com.microtomato.hirun.framework.harbour.excel.demo;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.harbour.excel.AbstractExcelHarbour;
import com.microtomato.hirun.framework.harbour.excel.ExcelConfig;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

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
    private IMenuService menuServiceImpl;

    @GetMapping("exportByTemplate")
    public void exportByTemplate(HttpServletResponse response) throws IOException {
        List<User> userList = userServiceImpl.list(Wrappers.<User>lambdaQuery().select(User::getUserId, User::getUsername).last("limit 100"));
        List<Menu> menuList = menuServiceImpl.list(Wrappers.<Menu>lambdaQuery().select(Menu::getMenuId, Menu::getTitle, Menu::getMenuUrl));
        Map<String, Object> fillMap = new HashMap(16);
        fillMap.put("now", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        fillMap.put("total", 1001);

        ExcelConfig excelConfig = ExcelConfig.builder()
            .fileName("Excel导出示例.xlsx")
            .templateFileName("demo.xlsx")
            .fillMap(fillMap)
            .sheet(Arrays.asList(0, 1))
            .lists(Arrays.asList(userList, menuList))
            .build();
        exportExcelByTemplate(response, excelConfig);
    }

    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userServiceImpl.list();
        List<UserExcelExportDTO> datas = new ArrayList<>(list.size());
        for (User user : list) {
            UserExcelExportDTO data = new UserExcelExportDTO();
            BeanUtils.copyProperties(user, data);
            datas.add(data);
        }

        // 调用基类导出函数
        exportExcel(response, "users", UserExcelExportDTO.class, datas, ExcelTypeEnum.XLSX);
    }

    /**
     * 下载导入模板
     *
     * @param response
     */
    @GetMapping(value = "download-template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 文件名
            String fileName = "社保缴纳数据导入模板";

            // Sheet名
            String sheetName = "员工数据";

            // 携带示例数据
            UserExcelImportDTO userExcelImportDTO = new UserExcelImportDTO();
            userExcelImportDTO.setName("小蕃茄头号悍匪");
            userExcelImportDTO.setCreateTime(LocalDateTime.now());
            List<UserExcelImportDTO> list = Arrays.asList(userExcelImportDTO);

            // 不携带示例数据
            List<UserExcelImportDTO> list2 = Arrays.asList();
            downloadImportTemplate(response, fileName, UserExcelImportDTO.class, list, sheetName, ExcelTypeEnum.XLSX);
        } catch (Exception e) {
            log.error("模板下载失败", e);
        }
    }

    /**
     * 导入数据
     *
     * @param multipartFile
     * @return 返回异常 batchId, 如果为 null，表示全部导入成功，如果不为空，通过 downloadErrorByBatchId 接口可以下载异常文件。
     * @throws IOException
     */
    @PostMapping("import")
    @RestResult
    public void importData(@RequestParam("fileUpload") MultipartFile multipartFile) throws IOException {

        UserReadListener listener = new UserReadListener();

        // 调用基类导入函数
        importExcel(multipartFile, UserExcelImportDTO.class, listener);
        isImportOk(listener.getErrBatchId());
    }

    /**
     * 通过批次号下载导入异常文件
     *
     * @param response
     * @param batchId
     * @throws Exception
     */
    @GetMapping(value = "download-error/{batchId}")
    public void downloadErrorByBatchId(HttpServletResponse response, @PathVariable(value = "batchId") String batchId) throws Exception {
        super.downloadError(response, batchId);
    }

}