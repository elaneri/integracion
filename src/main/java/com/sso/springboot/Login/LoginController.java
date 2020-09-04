package com.sso.springboot.Login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LoginController {
	

	@RequestMapping({ "/isAlive" })
	public String isAlive() {
				return "test";
	}


	
}