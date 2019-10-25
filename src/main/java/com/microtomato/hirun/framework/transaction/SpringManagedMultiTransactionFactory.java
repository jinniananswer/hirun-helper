package com.microtomato.hirun.framework.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

import javax.sql.DataSource;

/**
 * 本地多数据源事务控制工厂
 *
 * @author Steven
 * @date 2019-10-24
 */
@Slf4j
public class SpringManagedMultiTransactionFactory extends SpringManagedTransactionFactory {

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new SpringManagedMultiTransaction(dataSource);
    }

}
