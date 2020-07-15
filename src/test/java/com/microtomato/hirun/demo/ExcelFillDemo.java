package com.microtomato.hirun.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2019-12-03
 */
public class ExcelFillDemo {

    private static List<Staff> list() {
        List<Staff> list = new ArrayList<>();
        for (Long i = 1L; i < 10L; i++) {
            Staff staff = new Staff();
            staff.setId(i);
            staff.setMobileno("137" + RandomStringUtils.randomNumeric(8));
            staff.setHiredate(RandomStringUtils.randomAlphabetic(6));
            staff.setOnduty((i % 2 == 0) ? "在职" : "离职");
            staff.setSex((i % 2 == 1) ? "男" : "女");
            list.add(staff);
        }
        return list;
    }

    public static void main(String[] args) {
        String templateFileName = "E:/idea-workspace/hi-tech/hirun-helper/src/main/resources/excel-templates/demo.xlsx";

        // 方案1 根据对象填充
        String fileName = "C:\\Users\\Steven\\Desktop\\" + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭

        List<Staff> list = list();

        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
        excelWriter.fill(list, writeSheet);

        WriteSheet writeSheet1 = EasyExcel.writerSheet(1).build();
        excelWriter.fill(list, writeSheet1);

        // 千万别忘记关闭流
        excelWriter.finish();
    }
}
