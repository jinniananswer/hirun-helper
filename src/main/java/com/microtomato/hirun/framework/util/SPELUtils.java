package com.microtomato.hirun.framework.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @program: hirun-helper
 * @description: spring spel表达式执行工具
 * @author: jinnian
 * @create: 2020-05-21 14:48
 **/
public class SPELUtils {
    private SpelParserConfiguration configuration;

    private ExpressionParser parser;

    private EvaluationContext ctx;

    /**
     * 初始化，进行编译模式设置
     */
    public SPELUtils() {
        this.configuration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,SpelParserConfiguration.class.getClassLoader());
        this.parser = new SpelExpressionParser(configuration);
        this.ctx = new StandardEvaluationContext();
    }

    /**
     * 解析上下文变量
     * @param objects
     */
    public void parse(Object... objects) {
        if (objects == null || objects.length <= 0) {
            return;
        }

        for (Object object : objects) {
            String simpleName = object.getClass().getSimpleName();
            String variableName = StringUtils.firstCharToLower(simpleName);
            ctx.setVariable(variableName, object);
        }
    }

    /**
     * 执行逻辑表达式
     * @param boolExpression
     * @return
     */
    public boolean executeBool(String boolExpression) {
        boolean isMatch = (Boolean)parser.parseExpression(boolExpression).getValue(this.ctx);
        return isMatch;
    }

    /**
     * 执行逻辑表达式
     * @param boolExpression
     * @param objects
     * @return
     */
    public boolean executeBool(String boolExpression, Object... objects) {
        this.parse(objects);
        return this.executeBool(boolExpression);
    }

    /**
     * 执行计算表达式
     * @param expression
     * @param objects
     * @return
     */
    public Long executeLong(String expression, Object... objects) {
        this.parse(objects);
        Long result = Math.round(new Double(parser.parseExpression(expression).getValue(this.ctx).toString()));
        return result;
    }

    /**
     * 执行计算表达式
     * @param expression
     * @return
     */
    public Long executeLong(String expression) {
        Long result = (Long)parser.parseExpression(expression).getValue(this.ctx);
        return result;
    }

    /**
     * 获取变量解析上下文
     * @return
     */
    public EvaluationContext getContext() {
        return this.ctx;
    }
}
