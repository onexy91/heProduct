package com.pshc.util.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	public CustomLoginSuccessHandler(String defaultTargetUrl) {
		setDefaultTargetUrl(defaultTargetUrl);
	}
	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse response, 
											Authentication authentication) throws IOException, ServletException {
		HttpSession session = req.getSession();
		log.info("HttpSession : " + session);
		if (session != null) {
			String redirectUrl = (String) session.getAttribute("prevPage");
			log.info("redirectUrl : " + redirectUrl);
			if(redirectUrl != null) {
				session.removeAttribute("prevPage");
				getRedirectStrategy().sendRedirect(req, response, redirectUrl);
			}else {
				super.onAuthenticationSuccess(req, response, authentication);
			}
		}else {
			super.onAuthenticationSuccess(req, response, authentication);
		}
	}
}
