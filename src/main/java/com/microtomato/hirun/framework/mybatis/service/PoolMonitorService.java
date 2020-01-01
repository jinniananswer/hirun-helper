package com.microtomato.hirun.framework.mybatis.service;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
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
public class PoolMonitorService implements ApplicationContextAware {

    static {
        PoolMonitorService.Check sender = new PoolMonitorService.Check();
        sender.setDaemon(true);
        sender.start();
    }

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    private static class Check extends Thread {
        private Check() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    AtomikosNonXADataSourceBean sysDataSource = (AtomikosNonXADataSourceBean) ctx.getBean("sysDataSource");
                    AtomikosNonXADataSourceBean insDataSource = (AtomikosNonXADataSourceBean) ctx.getBean("insDataSource");

                    keepAlive(sysDataSource);
                    keepAlive(insDataSource);

                    Thread.sleep(60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void keepAlive(AtomikosNonXADataSourceBean ds) {
            int maxPoolSize = ds.getMaxPoolSize();

            for (int i = 0; i < maxPoolSize; i++) {
                Connection conn = null;
                try {
                    conn = ds.getConnection();
                    Statement statement = conn.createStatement();
                    statement.executeQuery("SELECT 1");
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
