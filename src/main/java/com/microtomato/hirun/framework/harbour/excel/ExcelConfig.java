package com.microtomato.hirun.framework.harbour.excel;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @date 2019-12-03
 */
@Data
@Builder
public class ExcelConfig {
    private String fileName;
    private String templateFileName;
    private Map<String, Object> fillMap;
    private List<Integer> sheet;
    private List<List<?>> lists = new ArrayList();
}
