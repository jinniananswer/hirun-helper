package com.microtomato.hirun.framework.mybatis.dynamic.policy.impl;

import com.microtomato.hirun.framework.mybatis.dynamic.policy.DynamicShardTablePolicy;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 静态分片策略，以指定的入参作为分片值。
 *
 * @author Steven
 * @date 2020-03-20
 */
@Component
public class ShardTableStaticPolicy extends DynamicShardTablePolicy {

    /**
     * 参数发现器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * Express语法解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 抽象匹配条件
     *
     * @param key Shard 注解里的内容
     * @return 是否匹配
     */
    @Override
    public boolean matches(String key) {
        return true;
    }

    /**
     * 抽象最终决定哪个分表
     *
     * @param invocation 方法执行信息
     * @param key        Shard 注解里的内容
     * @return 数据源名称
     */
    @Override
    public String doDetermineShardTable(MethodInvocation invocation, String key) {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        String variable = StringUtils.substringBetween(key, "{", "}");
        Expression expression = PARSER.parseExpression("#" + variable);
        Object value = expression.getValue(context);
        return value.toString();

    }

}
