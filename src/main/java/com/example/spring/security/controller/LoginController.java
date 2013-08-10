package com.example.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "login/index";
	}

	@RequestMapping("/login/error")
	public String loginError() {
		return "login/error";
	}
}
