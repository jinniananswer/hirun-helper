package com.microtomato.hirun.framework.harbour.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.util.JsonUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.ExcelImportError;
import com.microtomato.hirun.modules.system.service.IExcelImportErrorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel 导入导出抽象类，被业务级 Controller 类直接继承
 *
 * @author Steven
 * @date 2019-10-30
 */
@Slf4j
public abstract class AbstractExcelHarbour {

    /**
     * 头策略
     */
    private WriteCellStyle defaultHeadWriteCellStyle() {
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("Cambria");
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteFont.setFontHeightInPoints((short) 10);

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        return headWriteCellStyle;
    }

    /**
     * 内容策略
     */
    private WriteCellStyle defaultContentWriteCellStyle() {
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("Cambria");
        contentWriteFont.setFontHeightInPoints((short) 9);

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return contentWriteCellStyle;
    }

    /**
     * 以模板方式导出 Excel
     *
     * @param response
     * @param excelConfig 配置
     * @throws IOException
     */
    protected void exportExcelByTemplate(HttpServletResponse response, ExcelConfig excelConfig) throws IOException {

        String templateFileName = excelConfig.getTemplateFileName();
        List<Integer> sheet = excelConfig.getSheet();
        List<List<?>> lists = excelConfig.getLists();
        Map<String, Object> fillMap = excelConfig.getFillMap();

        File file = ResourceUtils.getFile("classpath:excel-templates/" + templateFileName);
        String templateAbsolutePath = file.getAbsolutePath();
        log.error("模板绝对路径：{}", templateAbsolutePath);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode(excelConfig.getFileName(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateAbsolutePath).build();

        for (int i = 0; i < sheet.size(); i++) {
            List<?> list = lists.get(i);
            WriteSheet writeSheet = EasyExcel.writerSheet(sheet.get(i)).build();
            excelWriter.fill(list, writeSheet);

            if (null != fillMap) {
                // excelWriter.fill(fillMap, writeSheet);;
            }
        }

        // 千万别忘记关闭流
        excelWriter.finish();

    }

    /**
     * 使用 XLSX 格式
     */
    protected void exportExcel(HttpServletResponse response, String fileName, Class dataType, List<?> list) throws IOException {
        exportExcel(response,
            fileName,
            dataType,
            list,
            "Sheet1",
            ExcelTypeEnum.XLSX,
            defaultHeadWriteCellStyle(),
            defaultContentWriteCellStyle());
    }

    /**
     * 使用默认样式
     */
    protected void exportExcel(HttpServletResponse response, String fileName, Class dataType, List<?> list, ExcelTypeEnum excelTypeEnum) throws IOException {
        exportExcel(response,
            fileName,
            dataType,
            list,
            "Sheet1",
            excelTypeEnum,
            defaultHeadWriteCellStyle(),
            defaultContentWriteCellStyle());
    }

    /**
     * @param response              HTTP 响应
     * @param fileName              文件名
     * @param dataType              数据类型
     * @param list                  数据列表
     * @param sheetName             Sheet 名称
     * @param excelTypeEnum         excel 文件类型
     * @param headWriteCellStyle    表头样式
     * @param contentWriteCellStyle 内容样式
     * @throws IOException 异常
     */
    protected void exportExcel(HttpServletResponse response,
                               String fileName,
                               Class dataType,
                               List<?> list,
                               String sheetName,
                               ExcelTypeEnum excelTypeEnum,
                               WriteCellStyle headWriteCellStyle,
                               WriteCellStyle contentWriteCellStyle) throws IOException {

        if ((!StringUtils.endsWith(fileName, ExcelTypeEnum.XLS.getValue())) &&
            (!StringUtils.endsWith(fileName, ExcelTypeEnum.XLSX.getValue()))) {
            fileName += excelTypeEnum.getValue();
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
            new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), dataType)
            .excelType(excelTypeEnum)
            .registerWriteHandler(horizontalCellStyleStrategy)
            .sheet(sheetName)
            .doWrite(list);
    }

    /**
     * 从 MultipartFile 中以流的方式获取数据
     * <p>
     * Excel 导入
     */
    protected void importExcel(MultipartFile multipartFile, Class dataType, ReadListener readListener) throws IOException {
        InputStream ins = multipartFile.getInputStream();
        EasyExcel.read(ins, dataType, readListener).sheet().doRead();
    }

    /**
     * 下载导入模板
     *
     * @param response
     * @param fileName
     * @param dataType
     * @param list
     * @param sheetName
     * @param excelTypeEnum
     * @throws IOException
     */
    protected void downloadImportTemplate(HttpServletResponse response, String fileName, Class dataType, List<?> list,
                                          String sheetName, ExcelTypeEnum excelTypeEnum) throws IOException {
        exportExcel(response, fileName, dataType, list, sheetName, excelTypeEnum,
            defaultHeadWriteCellStyle(), defaultContentWriteCellStyle());
    }

    /**
     * 导出异常数据
     *
     * @param response
     * @param batchId
     * @throws Exception
     */
    protected void downloadError(HttpServletResponse response, String batchId) throws Exception {
        IExcelImportErrorService excelImportErrorServiceImpl = SpringContextUtils.getBean(IExcelImportErrorService.class);
        List<ExcelImportError> list = excelImportErrorServiceImpl.list(
            Wrappers.<ExcelImportError>lambdaQuery()
                .select(ExcelImportError::getRowContent)
                .eq(ExcelImportError::getBatchId, batchId)
        );

        List<List<String>> rows = new ArrayList<>();
        for (ExcelImportError excelImportError : list) {
            String rowContent = excelImportError.getRowContent();
            List<String> row = JsonUtils.decode(rowContent, List.class);
            rows.add(row);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("异常数据", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).sheet("Sheet1").doWrite(rows);
    }

    /**
     * 是否导入成功？如果异常数据批次号不为空，那么说明导入遇到了问题，可以通过批次号查看异常数据
     *
     * @param batchId 异常数据批次号
     */
    protected void isImportOk(String batchId) {
        if (null == batchId) {
            return;
        } else {
            throw new IllegalArgumentException(batchId);
        }
    }
}
