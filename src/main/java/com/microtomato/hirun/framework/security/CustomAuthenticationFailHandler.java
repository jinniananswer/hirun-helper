package com.microtomato.hirun.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microtomato.hirun.framework.data.Result;
import com.microtomato.hirun.framework.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author Steven
 * @date 2019-09-10
 */
@Component
@Slf4j
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

		log.info("Authentication failure: {}", exception.getMessage());

		Result result = ResultUtil.failure(400001, "用户名或密码不正确！" + exception.getMessage());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}

}
