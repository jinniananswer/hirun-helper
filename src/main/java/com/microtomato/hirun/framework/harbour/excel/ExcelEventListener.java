package com.microtomato.hirun.framework.harbour.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.microtomato.hirun.framework.util.JsonUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.system.entity.po.ExcelImportError;
import com.microtomato.hirun.modules.system.service.IExcelImportErrorService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Steven
 * @date 2019-11-21
 */
@Slf4j
public abstract class ExcelEventListener<T> extends AnalysisEventListener<T> {

    /**
     * 错误批次号，对应 excel_import_error.batch_id
     */
    @Getter
    private String errBatchId = null;

    /**
     * 异常数据
     */
    @Getter
    protected List<List<String>> errDatas = new ArrayList<>();

    /**
     * 每读取一行，将 CellData 解析成业务 POJO对象发生错误时，会触发一次该函数
     * 框架会将 Excel 里每行数据会转成 TreeMap<Integer, CellData> 数据，key 为列下标。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {

        ReadRowHolder readRowHolder = context.readRowHolder();
        Integer rowIndex = readRowHolder.getRowIndex();
        TreeMap<Integer, CellData> rowAnalysisResult = (TreeMap<Integer, CellData>) readRowHolder.getCurrentRowAnalysisResult();

        List<String> row = new ArrayList<>();
        for (Integer key : rowAnalysisResult.keySet()) {
            CellData cellData = rowAnalysisResult.get(key);
            row.add(cellData.getStringValue());
        }

        row.add(String.format("第%d行：格式不正确！", rowIndex));
        errDatas.add(row);

    }

    /**
     * 最后触发
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (errDatas.size() > 0) {
            /**
             * 如果导入过程中，发现异常数据，则将数据进行入库处理
             */
            IExcelImportErrorService excelImportErrorServiceImpl = SpringContextUtils.getBean(IExcelImportErrorService.class);
            LocalDateTime now = LocalDateTime.now();
            errBatchId = UUID.randomUUID().toString();
            for (List<String> row : errDatas) {
                String rowContent = JsonUtils.encode(row);
                ExcelImportError excelImportError = new ExcelImportError();
                excelImportError.setBatchId(errBatchId);
                excelImportError.setRowContent(rowContent);
                excelImportError.setCreateTime(now);
                excelImportErrorServiceImpl.save(excelImportError);
            }
        }
    }

    /**
     * 异常数据
     *
     * @param context     上下文
     * @param t           POJO实例
     * @param checkResult 校验结果
     */
    protected void addErrData(AnalysisContext context, T t, String checkResult) {
        Map<Integer, Field> fieldTreeMap = new TreeMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (null == annotation) {
                continue;
            }

            fieldTreeMap.put(annotation.index(), field);
        }

        List<String> row = new ArrayList<>();
        for (Integer key : fieldTreeMap.keySet()) {
            Field field = fieldTreeMap.get(key);
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t.getClass());
                Method getMethod = pd.getReadMethod();
                Object o = getMethod.invoke(t);
                if (null == o) {
                    row.add("");
                } else {
                    row.add(o.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Integer rowIndex = context.readRowHolder().getRowIndex();

        if (null != checkResult) {
            row.add(String.format("第%d行：%s", rowIndex, checkResult));
        } else {
            row.add(String.format("第%d行：格式不正确！", rowIndex));
        }
        errDatas.add(row);
        log.error("{}", row);
    }

    /**
     * 异常数据
     *
     * @param t
     * @param context
     */
    protected void addErrData(AnalysisContext context, T t) {
        addErrData(context, t, null);
    }

}
