package com.microtomato.hirun.framework.harbour.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.microtomato.hirun.framework.harbour.excel.convert.LocalDateTimeConvert;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-10-30
 */
@Data
public class UserImportData {

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2, converter = LocalDateTimeConvert.class)
    private LocalDateTime createTime;

}
