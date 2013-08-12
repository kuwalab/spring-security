package com.example.spring.security.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/beans-webmvc.xml",
		"file:src/main/webapp/WEB-INF/spring/security.xml" })
public class TestControllerTest {
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private MockHttpSession session;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(wac).addFilter(springSecurityFilterChain)
				.build();
		session = new MockHttpSession(wac.getServletContext(), UUID
				.randomUUID().toString());
	}

	@Test
	public void slash_testへのGET() throws Exception {
		mockMvc.perform(get("/test/")).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login")); // 絶対パスになる？
	}

	@Test
	public void slash_loginへ() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(view().name("login/index"))
				.andExpect(model().hasNoErrors());
	}

	@Test
	public void shash_testへアクセス後ログイン() throws Exception {
		mockMvc.perform(get("/test/").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
		// リダイレクトは、最初にアクセスした、/test/になる
		mockMvc.perform(
				post("/j_spring_security_check").param("j_username", "user1")
						.param("j_password", "user1").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/test/"));
	}

	@Test
	public void shash_testへアクセス後ログインしログアウト後に再度testへアクセス() throws Exception {
		mockMvc.perform(get("/test/").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
		// リダイレクトは、最初にアクセスした、/test/になる
		mockMvc.perform(
				post("/j_spring_security_check").param("j_username", "user1")
						.param("j_password", "user1").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/test/"));
		// ルートに飛ばされる
		mockMvc.perform(get("/logout").session(session))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/"));
		// 認証済みではなくなる
		mockMvc.perform(get("/test/").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void shash_loginへアクセス後ログイン() throws Exception {
		mockMvc.perform(get("/login").session(session)).andExpect(
				status().isOk());
		// リダイレクトは、/test/user1になる
		mockMvc.perform(
				post("/j_spring_security_check").param("j_username", "user1")
						.param("j_password", "user1").session(session))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/test/user1"));
	}
}
