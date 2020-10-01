package com.sso.springboot.JWT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sso.springboot.Messages.JWTMessages;
import com.sso.springboot.Messages.SSOError;
import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.UserClaims.UserClaimsService;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/JWT")
public class JWTController {

	@Autowired
	private UsuarioService userService;

	@Autowired
	private UserClaimsService userClaimService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger LOG = LoggerFactory.getLogger(JWTController.class);

	@RequestMapping(value = "/isAlive", method = RequestMethod.GET)
	public ResponseEntity<?> loginUsuario(@RequestHeader("x-api-key") String apk,
			@RequestBody JwtRequest authenticationRequest) throws Exception {

		return ResponseEntity.ok(JWTMessages.JWT_VALIDO);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) throws Exception {

		LOG.info("Llamada a refresh de TOKEN");
		String newtoken = "";
		String userId = "";

		token = token.replace(JwtTokenUtil.BEARER, "");

		try {
			jwtTokenUtil.isTokenExpired(token);
		} catch (ExpiredJwtException e) {
			if (jwtTokenUtil.ignoreTokenExpiration(e.getClaims())) {
				userId = String.valueOf(e.getClaims().get("sub"));
			}
			LOG.warn(SSOError.JWT_EXPIRADO.toString());
		}
		if (userId == null) {
			LOG.info(SSOError.USUARIO_INVALIDO.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOError.USUARIO_INVALIDO.toString());
		} else {
			Optional<Usuario> usuario = userService.findByUserIdAndTenant(userId);

			if (!usuario.isPresent()) {
				// TODO: usuario inválido o deshabilitado??? o no encontrado????
				LOG.info(SSOError.USUARIO_INVALIDO.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOError.USUARIO_INVALIDO.toString());
			} else {
				Map<String, Object> claims = new HashMap<>();
				List<UserClaims> userClaims = userClaimService.findClaimsForUser(usuario.get());

				for (UserClaims c : userClaims) {

					claims.put(c.getClaim().getNombre(), c.getClaimValue());

				}
				claims.put("client_id", usuario.get().getIdUsuario());
				claims.put("iss", JwtTokenUtil.ISS);
				String idUsuario = String.valueOf(usuario.get().getIdUsuario());

				newtoken = JwtTokenUtil.BEARER + jwtTokenUtil.refreshToken(idUsuario, claims);

				LOG.info("Token generado para usuario " + usuario.get().getNombre() + newtoken);
			}
		}
		return ResponseEntity.ok(new JwtResponse(newtoken));
	}
}
