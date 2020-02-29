package com.microtomato.hirun.framework.mybatis.processor.impl;

import com.microtomato.hirun.framework.mybatis.processor.DataSourceProcessor;
import com.microtomato.hirun.framework.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * 支持 SpringEL 表达式
 *
 * @author Steven
 * @date 2020-02-29
 */
@Slf4j
public class DataSourceSpelProcessor extends DataSourceProcessor {

    /**
     * 参数发现器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * Express语法解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    public DataSourceSpelProcessor() {
        log.info("构造动态数据源处理器: {}", PackageUtils.compactPackage(this.getClass()));
    }

    @Override
    public boolean matches(String key) {
        return true;
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {

        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();

        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        Expression expression = PARSER.parseExpression(key);
        Object value = expression.getValue(context);

        log.debug("解析数据源: {} -> {}", key, value);
        return value == null ? null : value.toString();
    }

}