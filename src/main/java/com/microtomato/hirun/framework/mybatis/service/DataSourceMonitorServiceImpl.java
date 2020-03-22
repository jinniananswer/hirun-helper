package com.microtomato.hirun.framework.mybatis.service;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接池保活
 *
 * @author Steven
 * @date 2020-01-01
 */
@Slf4j
@Service
public class DataSourceMonitorServiceImpl implements ApplicationContextAware {

    static {
        Checker checker = new Checker();
        checker.setDaemon(true);
        checker.start();
    }

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    private static class Checker extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(60000);
                    AtomikosDataSourceBean sysDataSource = (AtomikosDataSourceBean) ctx.getBean("sysDataSource");
                    AtomikosDataSourceBean insDataSource = (AtomikosDataSourceBean) ctx.getBean("insDataSource");

                    keepAlive(sysDataSource);
                    keepAlive(insDataSource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void keepAlive(AtomikosDataSourceBean ds) {
            int poolAvailableSize = ds.poolAvailableSize();
            for (int i = 0; i < poolAvailableSize; i++) {
                Connection conn = null;
                Statement statement = null;
                try {
                    conn = ds.getConnection();
                    statement = conn.createStatement();
                    statement.executeQuery("SELECT 1");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(statement);
                    IOUtils.closeQuietly(conn);
                }
            }
        }
    }

}
