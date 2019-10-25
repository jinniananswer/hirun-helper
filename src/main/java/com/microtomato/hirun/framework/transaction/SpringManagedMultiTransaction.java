package com.microtomato.hirun.framework.transaction;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO: SpringManagedMultiTransaction 是否在一个线程上下文里？
 *
 * @author Steven
 * @date 2019-10-24
 */
@Slf4j
public class SpringManagedMultiTransaction implements Transaction {

    /**
     * 一个 com.baomidou.dynamic.datasource.DynamicRoutingDataSource 实例！
     */
    private final DataSource dataSource;

    /**
     * 当前事务拥有的连接
     */
    private ConcurrentMap<String, Connection> connectionMap;

    public SpringManagedMultiTransaction(DataSource dataSource) {
        Assert.notNull(dataSource, "No DataSource specified");
        this.connectionMap = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
        if (log.isDebugEnabled()) {
            log.debug("DataSource of Type :: class {}", this.dataSource.getClass());
        }
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;

        String currDsName = DynamicDataSourceContextHolder.peek();
        log.debug("DynamicDataSourceContextHolder.peek, currDsName: {}", currDsName);

        if (null != currDsName && connectionMap.containsKey(currDsName)) {
            conn = connectionMap.get(currDsName);
            if (log.isDebugEnabled()) {
                log.debug("返回当前事务上下文中的连接: {} -> {}", currDsName, conn);
            }
            return conn;
        }

        try {
            // DynamicRoutingDataSource 内部的 determineDataSource 函数会返回当前线程上下文中正确的数据源连接对象！
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            if (null != currDsName) {
                this.connectionMap.put(currDsName, conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.debug("当前事务控制器对象: " + this);
        return conn;
    }

    @Override
    public void commit() {
        boolean isFailure = false;
        for (Connection connection : connectionMap.values()) {
            try {
                if (isFailure) {
                    connection.rollback();
                    continue;
                }
                connection.commit();
                log.debug("Committing {}", connection);
            } catch (SQLException e) {
                isFailure = true;
                log.error("Committing error！connection: " + connection, e);
            }
        }
    }

    @Override
    public void rollback() {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.rollback();
                log.debug("Rolling back {}", connection);
            } catch (SQLException e) {
                log.error("Rolling back error！connection: " + connection, e);
            }
        }
    }

    @Override
    public void close() {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Release Connection error: {}", connection);
            }
            log.debug("Release Connection: {}", connection);
        }
    }

    @Override
    public Integer getTimeout() {
        return null;
    }
}