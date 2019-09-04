package com.microtomato.hirun.framework.aop;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 拦截 RestController方法默认返回参数，统一处理返回值/响应体，配合 RestResult.class 使用。
 *
 * @author Steven
 * @date 2019-04-26
 */
@Slf4j
@RestControllerAdvice
public class RestResultAdvice implements ResponseBodyAdvice {

	@Override
	public boolean supports(MethodParameter methodParameter, Class converterType) {
		return methodParameter.hasMethodAnnotation(RestResult.class);
	}

	@Override
	public Object beforeBodyWrite(Object body,
	                              MethodParameter returnType,
	                              MediaType selectedContentType,
	                              Class selectedConverterType,
	                              ServerHttpRequest request,
	                              ServerHttpResponse response) {
		return ResultUtil.success(body);

	}
}
