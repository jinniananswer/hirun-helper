package com.microtomato.hirun.framework.mybatis.service;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.microtomato.hirun.framework.mybatis.DataSourceWrapper;
import com.microtomato.hirun.framework.mybatis.MyGlobalConfig;
import com.microtomato.hirun.framework.mybatis.MySqlSessionTemplate;
import com.microtomato.hirun.framework.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

/**
 * 数据库连接池保活
 *
 * @author Steven
 * @date 2020-01-01
 */
@Slf4j
@Service
public class DataSourcePingServiceImpl implements ApplicationContextAware {

    static {
        Checker checker = new Checker();
        checker.setDaemon(true);
        checker.start();
    }

    private static ApplicationContext ctx;

    /** 连接池心跳间隔，单位：秒 */
    private static int pingInterval;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        String pingIntervalString = ctx.getEnvironment().getProperty("hirun.datasource.pingInterval", "15");
        pingInterval = Integer.parseInt(pingIntervalString);
        log.trace("连接池检查心跳间隔: {}s", pingInterval);
    }

    private static class Checker extends Thread {

        @Override
        public void run() {

            while (true) {

                try {

                    Thread.sleep(pingInterval * 1000);
                    if (null == ctx) {
                        continue;
                    }

                    MyGlobalConfig myGlobalConfig = ctx.getBean(MyGlobalConfig.class);
                    MySqlSessionTemplate sqlSessionTemplate = myGlobalConfig.getSqlSessionTemplate();
                    Map<String, DataSourceWrapper> dataSourceWrapperMap = sqlSessionTemplate.getTargetDataSourceWrappers();
                    for (String dataSource : dataSourceWrapperMap.keySet()) {
                        log.trace("连接池心跳检查开始: {}", dataSource);
                        long start = System.currentTimeMillis();
                        DataSourceWrapper dataSourceWrapper = dataSourceWrapperMap.get(dataSource);
                        keepAlive(dataSourceWrapper);
                        long cost = System.currentTimeMillis() - start;
                        log.trace("连接池心跳检查完成: {}, 耗时: {} ms", dataSource, cost);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void keepAlive(DataSourceWrapper dataSourceWrapper) {

            AtomikosNonXADataSourceBean ads = dataSourceWrapper.getAtomikosNonXADataSourceBean();
            String validationQuery = dataSourceWrapper.getValidationQuery();

            if (null == ads) {
                return;
            }

            Connection conn = null;
            Statement stmt = null;
            try {

                /**
                 * atomikos 连接池的内部实现基于 ArrayList，每次都是从第一个迭代，直至找到一个可用的连接返回。
                 * 因此这里保证不了池内的所有连接都测试一遍，只是拿池内的一个代表出来探测，如果成功代表没有问题，
                 * 如果失败就对连接池进行重置。
                 */
                conn = ads.getConnection();
                if (null == conn) {
                    return;
                }

                stmt = conn.createStatement();
                stmt.execute(validationQuery);

            } catch (Exception e) {
                e.printStackTrace();
                /** 连接池重置 */
                log.warn("重置连接池: {}", ads);
                ads.refreshPool();
            } finally {
                IOUtils.closeQuietly(stmt);
                IOUtils.closeQuietly(conn);
            }

        }

    }

}
