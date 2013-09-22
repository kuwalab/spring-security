package com.example.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 参考 http://d.hatena.ne.jp/ocs/20111207/1323269801
 */
public class CustomAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String url = authentication.getName();
		if (url.equals("admin")) {
			url = "index";
		}
		response.sendRedirect(request.getContextPath() + "/test/" + url);
	}
}
