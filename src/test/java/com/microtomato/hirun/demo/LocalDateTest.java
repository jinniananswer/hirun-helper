package com.microtomato.hirun.demo;

import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Steven
 * @date 2020-05-31
 */
public class LocalDateTest {
    public static void main(String[] args) {

        for (int i = 1; i <= 31; i++) {
            test("1988-12-" + StringUtils.leftPad(String.valueOf(i), 2, '0'));
        }
    }

    private static void test(String strDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayLocalDate = LocalDate.parse(strDate, fmt);
        SimpleDateFormat ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
        //SimpleDateFormat ddf = new SimpleDateFormat("''yyyy-MM-dd''");
        //TimeZone tz = TimeZone.getTimeZone("GMT+8");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        ddf.setTimeZone(tz);
        Date date = Date.valueOf(birthdayLocalDate);
        System.out.print(strDate + " -> ");
        String format = ddf.format(date);
        System.out.print(format);
        System.out.println(" " + (format.indexOf(strDate) != -1 ? "正确" : "错误"));
    }
}
