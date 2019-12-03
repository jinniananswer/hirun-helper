package com.microtomato.hirun.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Steven
 * @date 2019-12-03
 */
@Data
public class Staff {

    @ExcelProperty(index = 1)
    private Long id;

    @ExcelProperty(index = 2)
    private String mobileno;

    @ExcelProperty(index = 3)
    private String hiredate;

    @ExcelProperty(index = 4)
    private String onduty;

    @ExcelProperty(index = 5)
    private String sex;
}
