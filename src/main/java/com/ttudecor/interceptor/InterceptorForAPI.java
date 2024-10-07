package com.ttudecor.interceptor;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ttudecor.utils.TokenUtils;

@Component
public class InterceptorForAPI implements HandlerInterceptor{

	@Autowired
	private HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String authorization = request.getHeader("Authorization");
		String validateStatus = TokenUtils.validateToken(authorization);
		if(validateStatus.equals("OK"))
			return true;
		else {
			System.out.println("Token validate failed: " + validateStatus);
			return false;
		}
//		
//		if(session.getAttribute("fullname") != null) {
//			
//			if(session.getAttribute("admin") != null) return true;
//			else {
//				response.sendRedirect("/home");
//				return false;
//			}
//		}else {
//			String uri = request.getRequestURI();
//			System.out.println("Login unsuccessful: " + uri);
//			
//			session.setAttribute("redirectUri", uri);
//			response.sendRedirect("/login");
//			return false;
//		}

	}

	
	
}
