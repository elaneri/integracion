package com.sso.springboot.Login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.sso.springboot.JWT.JwtRequest;
import com.sso.springboot.JWT.JwtResponse;
import com.sso.springboot.JWT.JwtTokenUtil;
import com.sso.springboot.Messages.SSOMessages;
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

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@PostMapping
	public ResponseEntity<?> loginUsuario(@RequestHeader("x-api-key") String apk,
			@RequestBody JwtRequest authenticationRequest) throws Exception {

		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword());

			authToken.setDetails(apk);

			authenticationManager.authenticate(authToken);

			/* esta parte se ejecuta si authenticationManager esta ok */
			Optional<Usuario> usuario = userService.findByUserNameAndTenant(authenticationRequest.getUsername(), apk);

			Map<String, Object> claims = new HashMap<>();
			List<UserClaims> userClaims = userClaimService.findClaimsForUser(usuario.get());

			for (UserClaims c : userClaims) {
				claims.put(c.getClaim().getNombre(), c.getClaimValue());
			}
			claims.put("client_id", usuario.get().getIdUsuario());
			claims.put("iss", JwtTokenUtil.ISS);
			String idUsuario = String.valueOf(usuario.get().getIdUsuario());
			final String token = JwtTokenUtil.BEARER + jwtTokenUtil.generateToken(idUsuario, claims);

			return ResponseEntity.ok(new JwtResponse(token));

		} catch (DisabledException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_DESHABILITADO.toString(), e);
		} catch (BadCredentialsException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, SSOMessages.CREDENCIALES_INVALIDAS.toString(), e);
		}
	}
}