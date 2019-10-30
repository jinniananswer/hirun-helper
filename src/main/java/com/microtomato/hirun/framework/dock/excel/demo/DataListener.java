package com.microtomato.hirun.framework.dock.excel.demo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2019-10-30
 */
@Slf4j
public class DataListener extends AnalysisEventListener<UserImportData> {

    private static final int BATCH_COUNT = 50;

    private List<Steven> list = new ArrayList<>(BATCH_COUNT);
    private IStevenService stevenServiceImpl;

    public DataListener(IStevenService stevenServiceImpl) {
        this.stevenServiceImpl = stevenServiceImpl;
    }

    @Override
    public void invoke(UserImportData data, AnalysisContext context) {
        Steven steven = new Steven();
        BeanUtils.copyProperties(data, steven);
        list.add(steven);

        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        stevenServiceImpl.saveBatch(list, list.size());
        log.info("存储数据库成功！");
    }

}