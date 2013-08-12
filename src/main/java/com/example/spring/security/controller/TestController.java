package com.example.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {
	@RequestMapping("/")
	public String index() {
		return "test/index";
	}

	@RequestMapping("/user1")
	public String user1() {
		return "test/user1";
	}

	@RequestMapping("/user2")
	public String user2() {
		return "test/user2";
	}
}
