package com.microtomato.hirun.framework.harbour.excel.demo;

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
public class UserReadListener extends AnalysisEventListener<UserImportData> {

    private static final int BATCH_COUNT = 5;

    private List<Steven> list = new ArrayList<>(BATCH_COUNT);
    private IStevenService stevenServiceImpl;

    public UserReadListener(IStevenService stevenServiceImpl) {
        this.stevenServiceImpl = stevenServiceImpl;
    }

    /**
     * 每读取一行触发一次该函数
     *
     * @param data excel 读过来的一行数据
     * @param context 上下文信息
     */
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

    /**
     * 最后触发
     */
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
        for (Steven steven : list) {
            stevenServiceImpl.save(steven);
        }
        log.info("存储数据库成功！");
    }

}