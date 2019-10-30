package com.microtomato.hirun.framework.export.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.microtomato.hirun.framework.export.convert.LocalDateTimeConvert;
import lombok.Data;

import java.time.LocalDateTime;

/**
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
