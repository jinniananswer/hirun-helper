package com.microtomato.hirun.modules;

import com.microtomato.hirun.framework.utils.WebContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 菜单导航控制器
 *
 * @program: hirun
 * @description: 鸿助手后台管理系统主控制器（含首页，登陆页面等）
 * @author: jinnian
 * @create: 2019-07-28 18:27
 **/
@Controller
public class MainController {

	private static final String OPEN_URL = "openUrl?url=";

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/console")
	public String console() {
		return "/modules/system/console";
	}

	@GetMapping("/openUrl")
	public String openUrl(HttpServletRequest request) {

		String url = request.getParameter("url");

		// 添加 URL 校验，防止手工拼菜单地址。
		Set<String> menuUrls = WebContextUtil.getUserContext().getMenuUrls();
		if (menuUrls.contains(OPEN_URL + url)) {
			return url;
		} else {
			return "/error/no-right";
		}

	}
}
