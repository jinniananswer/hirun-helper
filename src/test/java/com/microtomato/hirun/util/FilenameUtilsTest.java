package com.microtomato.hirun.util;

import org.apache.commons.io.FilenameUtils;

/**
 * @author Steven
 * @date 2020-08-30
 */
public class FilenameUtilsTest {
    public static void main(String[] args) {
        String destPath = FilenameUtils.concat("/home/web/data",  "upload" + "/order");
        System.out.println(destPath);
    }
}
