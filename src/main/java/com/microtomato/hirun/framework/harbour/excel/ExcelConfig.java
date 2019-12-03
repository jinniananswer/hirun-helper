package com.microtomato.hirun.framework.harbour.excel;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 配置类
 *
 * @author Steven
 * @date 2019-12-03
 */
@Data
@Builder
public class ExcelConfig {

    /**
     * 导出文件名
     */
    private String fileName;

    /**
     * 模板文件名，模板统一存放在：resources/excel-templates 目录下
     */
    private String templateFileName;

    /**
     * 填充模板的数据，扁平结构
     */
    private Map<String, Object> fillMap;

    /**
     * 模板的 sheet 页下标，从 0 开始。
     */
    private List<Integer> sheet;

    /**
     * 填充模板的数据，列表结果
     */
    private List<List<?>> lists;
}
