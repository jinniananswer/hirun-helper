package com.microtomato.hirun.framework.mybatis;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Steven
 * @date 2020-10-22
 */
public class DataSourceWrapper {
    @Getter
    @Setter
    private AtomikosNonXADataSourceBean atomikosNonXADataSourceBean;

    @Setter
    private String validationQuery;

    public String getValidationQuery() {
        if (null == validationQuery) {
            return judgeValidationQuery();
        }
        return this.validationQuery;
    }

    /**
     * 根据配置的数据库驱动，返回探测语句
     *
     * @return
     */
    private String judgeValidationQuery() {
        String driverClassName = this.atomikosNonXADataSourceBean.getDriverClassName();
        if (StringUtils.contains(driverClassName, "mysql")) {
            return "select 1";
        } else if (StringUtils.contains(driverClassName, "oracle")) {
            return "select 1 from dual";
        }
        throw new IllegalArgumentException("无法识别的数据库驱动: " + driverClassName);
    }
}
