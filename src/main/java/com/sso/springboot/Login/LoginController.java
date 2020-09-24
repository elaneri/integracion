package com.sso.springboot.Login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.JWT.JwtRequest;
import com.sso.springboot.JWT.JwtResponse;
import com.sso.springboot.JWT.JwtTokenUtil;
import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.UserClaims.UserClaimsService;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

@RestController
@RequestMapping("/Login")
public class LoginController {

	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private UserClaimsService userClaimService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping
	public ResponseEntity<?> loginUsuario(@RequestHeader("x-api-key") String apk,
			@RequestBody JwtRequest authenticationRequest) throws Exception {

		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword());

			authToken.setDetails(apk);

			authenticationManager.authenticate(authToken);

			/* esta parte se ejecuta si authenticationManager esta ok */
			Optional<Usuario> usuario = userService.findByUserName(authenticationRequest.getUsername());

			Map<String, Object> claims = new HashMap<>();
			List<UserClaims> userClaims = userClaimService.findClaimsForUser(usuario.get());
			
			for (UserClaims c : userClaims) {

				claims.put(c.getClaim().getNombre(), c.getClaimValue());

			}
			claims.put("client_id", usuario.get().getIdUsuario());
			claims.put("iss", JwtTokenUtil.ISS);

			final String token = JwtTokenUtil.BEARER + jwtTokenUtil.generateToken(usuario.get().getUsuario(), claims);

			return ResponseEntity.ok(new JwtResponse(token));

		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}