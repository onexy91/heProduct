package com.pshc.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.pshc.util.security.service.CustomLoginSuccessHandler;
import com.pshc.util.security.service.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService service;

	@Bean
	public PasswordEncoder pEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new CustomLoginSuccessHandler("/main");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		//log.info("띵");
		auth.userDetailsService(service).passwordEncoder(pEncoder());
		//System.out.println(service.loadUserByUsername("PHJ").getAuthorities());
		
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**", "/js/**", "image/**", "/fonts/**");
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// 접속권한 정의
				.antMatchers("/login", "/error", "/h2-console/**", "/", "/sign").permitAll()
				.antMatchers("/main", "/posts", "/filedown", "/updatepost").hasRole("USER")
				// .anyRequest().authenticated()
				.and()
				.csrf().ignoringAntMatchers("/h2-console/**", "/filedown", "/updatepost") // 여기!
				.and()
				// 로그인 페이지 및 성공 URL, handler, 로그인시 사용되는 id, pass
				.formLogin()
				.loginPage("/") //view
				.loginProcessingUrl("/") //로그인 URL (/) 이거로 되어있어 실제로 Process탐 
				.defaultSuccessUrl("/main")
				.successHandler(successHandler())
				.failureUrl("/")
				.and()
				// 로그아웃관련 설정
				.logout().permitAll();
		http.headers().frameOptions().disable();
		
	}

}
