package com.microtomato.hirun.framework.dock.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.microtomato.hirun.framework.dock.excel.demo.UserExportData;
import com.microtomato.hirun.framework.dock.excel.demo.DataListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Steven
 * @date 2019-10-30
 */
public abstract class AbstractExcelImportController {

    protected void importExcel(MultipartFile multipartFile, Class dataType, ReadListener readListener) throws IOException {
        InputStream ins = multipartFile.getInputStream();
        EasyExcel.read(ins, dataType, readListener).sheet().doRead();
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Steven\\Downloads" + File.separator + "users.xls";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        //EasyExcel.read(fileName, UserExportData.class, new DataListener()).sheet().doRead();
    }

}
