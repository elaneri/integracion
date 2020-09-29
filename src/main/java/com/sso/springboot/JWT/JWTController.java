package com.sso.springboot.JWT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

		token = token.replace(JwtTokenUtil.BEARER, "");
		Optional<Usuario> usuario = userService.findByUserIdAndTenant(jwtTokenUtil.getUserIdFromToken(token));

		if (!usuario.isPresent()) {
			LOG.info("usuario invalido");
			
			throw new Exception("USER_DISABLED");

		} else {

			

			Map<String, Object> claims = new HashMap<>();
			List<UserClaims> userClaims = userClaimService.findClaimsForUser(usuario.get());

			for (UserClaims c : userClaims) {

				claims.put(c.getClaim().getNombre(), c.getClaimValue());

			}
			claims.put("client_id", usuario.get().getIdUsuario());
			claims.put("iss", JwtTokenUtil.ISS);
			String idUsuario = String.valueOf(usuario.get().getIdUsuario());

			newtoken = JwtTokenUtil.BEARER + jwtTokenUtil.generateToken(idUsuario, claims);
			
			LOG.info("Token generado para usuario " +usuario.get().getNombre() +newtoken);

		}

		return ResponseEntity.ok(new JwtResponse(newtoken));
	}
}
