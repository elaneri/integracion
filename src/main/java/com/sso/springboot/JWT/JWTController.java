package com.sso.springboot.JWT;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.Messages.JWTMessages;



@RestController
@RequestMapping("/isValid")
public class JWTController {

	@PostMapping
	public ResponseEntity<?> loginUsuario(@RequestHeader("x-api-key") String apk,
			@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		
		
		return ResponseEntity.ok(JWTMessages.JWT_VALIDO);
	}
}
