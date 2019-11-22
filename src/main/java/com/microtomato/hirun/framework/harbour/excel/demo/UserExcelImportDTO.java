package com.microtomato.hirun.framework.harbour.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.microtomato.hirun.framework.harbour.excel.convert.LocalDateTimeConvert;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-10-30
 */
@Data
public class UserExcelImportDTO {

    @NotNull(message = "用户名不能为空！")
    @Size(message = "用户名长度不合法，有效范围: 11位！", min = 11, max = 11)
    @Digits(message = "用户名必须是11位长度的数字！", integer = 11, fraction = 0)
    @ColumnWidth(25)
    @ExcelProperty(value = "用户名", index = 0)
    private String name;

    @NotNull(message = "注册时间不能为空！")
    @ColumnWidth(25)
    @ExcelProperty(value = "注册时间", index = 1, converter = LocalDateTimeConvert.class)
    private LocalDateTime createTime;

}
