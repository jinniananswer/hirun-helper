package com.microtomato.hirun.framework.harbour.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.microtomato.hirun.framework.harbour.excel.convert.LocalDateTimeConvert;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导出数据定义，格式，表头，格式转换等
 *
 * @author Steven
 * @date 2019-10-30
 */
@Data
public class UserExportData {

    @ColumnWidth(20)
    @ExcelProperty("用户ID")
    private Long userId;

    @ColumnWidth(20)
    @ExcelProperty("用户名")
    private String username;

    @ColumnWidth(20)
    @ExcelProperty(value = "注册时间", converter = LocalDateTimeConvert.class)
    private LocalDateTime createTime;

}
