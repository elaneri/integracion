package com.sso.springboot.JWT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.Messages.JWTMessages;
import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.UserClaims.UserClaimsService;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;



@RestController
@RequestMapping("/JWT")
public class JWTController {
	
	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private UserClaimsService userClaimService;
	
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@RequestMapping(value = "/isAlive", method = RequestMethod.GET)
	public ResponseEntity<?> loginUsuario(@RequestHeader("x-api-key") String apk,
			@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		
		
		return ResponseEntity.ok(JWTMessages.JWT_VALIDO);
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> refreshToken(@RequestHeader("x-api-key") String apk,
			@RequestHeader("Authorization") String token) throws Exception {
		
		
		token = token.replace(JwtTokenUtil.BEARER, "");
		Optional<Usuario> usuario = userService.findByUserName(jwtTokenUtil.getUsernameFromToken(token));
	
		Map<String, Object> claims = new HashMap<>();
		List<UserClaims> userClaims = userClaimService.findClaimsForUser(usuario.get());
		
		for (UserClaims c : userClaims) {

			claims.put(c.getClaim().getNombre(), c.getClaimValue());

		}
		claims.put("client_id", usuario.get().getIdUsuario());
		claims.put("iss", JwtTokenUtil.ISS);

		final String newtoken = JwtTokenUtil.BEARER + jwtTokenUtil.generateToken(usuario.get().getUsuario(), claims);

		return ResponseEntity.ok(new JwtResponse(newtoken));
	}
}
