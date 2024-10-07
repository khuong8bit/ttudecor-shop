package com.ttudecor.controller.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;
import com.ttudecor.utils.TokenUtils;

@RestController
public class LoginAPI {
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpServletResponse response;
	
	@PostMapping("/api/login")
	public String login(@RequestBody User userLogin){
		
		User user = userService.findByEmailAndPassword(userLogin.getEmail(), userLogin.getPassword());
		
		if(user != null) { //login successfully
			String token = TokenUtils.generateToken(user.getId().toString());
			TokenUtils.setTokenResponse(response, token);
			return "Token: " + token;
		}
		else return "LOGIN_FAIL";
			
	}
}
