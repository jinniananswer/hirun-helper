package com.microtomato.hirun.framework.dock.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel 导出抽象类
 *
 * @author Steven
 * @date 2019-10-30
 */
@Slf4j
public class AbstractExcelExportController {

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

    protected void export(HttpServletResponse response, String fileName, Class dataType, List<?> list) throws IOException {
        export(response,
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
    protected void export(HttpServletResponse response, String fileName, Class dataType, List<?> list, ExcelTypeEnum excelTypeEnum) throws IOException {
        export(response,
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
    protected void export(HttpServletResponse response,
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
}
