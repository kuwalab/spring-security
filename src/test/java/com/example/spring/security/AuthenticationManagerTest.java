package com.example.spring.security;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// ContextConfigurationを指定するとデフォルトて、クラス名-context.xmlが設定として読み込まれる。
@ContextConfiguration
public class AuthenticationManagerTest {
	@Autowired
	private ApplicationContext context;
	private AuthenticationManager am;

	@Before
	public void setUp() {
		am = (AuthenticationManager) context.getBean("authenticationManager");
	}

	private void login(String username, String password) {
		Authentication request = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication result = am.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}

	@After
	public void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	public void 認証成功のテスト() {
		login("user1", "user1");
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		assertThat(authentication.isAuthenticated(), is(true));
	}

	@Test
	public void 認証失敗のテスト() {
		try {
			login("user1", "user2");
			// 認証失敗は例外
			fail();
		} catch (BadCredentialsException e) {
		}
		// 認証失敗だと、認証オブジェクトが取得できない。null
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		assertThat(authentication, is(nullValue()));
	}
}
