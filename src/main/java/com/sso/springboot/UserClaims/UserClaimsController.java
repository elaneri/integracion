package com.sso.springboot.UserClaims;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Claims.ClaimsServiceImpl;
import com.sso.springboot.Messages.JWTMessages;
import com.sso.springboot.Messages.SSOError;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioServiceImpl;

@RestController
@RequestMapping("/Claims")
public class UserClaimsController {

	@Autowired
	private UsuarioServiceImpl usuarioService;

	@Autowired
	private ClaimsServiceImpl claimService;

	@Autowired
	private UserClaimsServiceImpl userClaimService;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserClaimsController.class);

	@RequestMapping(value = "/ValidClaims", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Claim>> getUserClaims() throws Exception {
		List<Claim> claims = claimService.getValidClaims();
		return ResponseEntity.ok(claims);
	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserClaims>> getUserClaims(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {
		Optional<Usuario> us = usuarioService.findById(idUsuario);
		List<UserClaims> cls = new ArrayList<>();
		if (us.isPresent()) {
			cls = userClaimService.findClaimsForUser(us.get());
		}
		return ResponseEntity.ok(cls);
	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addClaim(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody RequestClaim claim) throws Exception {

		Optional<Usuario> us = usuarioService.findById(idUsuario);

		if (claim.getValor().length() > 10) {
			LOG.warn(SSOError.CLAIM_VALOR_10_CARACTERES.toString());
			throw new Exception(SSOError.CLAIM_VALOR_10_CARACTERES.toString());
		}
		
		List<UserClaims> claims = new ArrayList<UserClaims>();
		claims = userClaimService.findClaimsForUser(us.get());
		
		if (!claims.isEmpty() &&  claims.size() > 10) {
			LOG.warn(SSOError.CLAIM_CANTIDAD_10.toString());
			throw new Exception(SSOError.CLAIM_CANTIDAD_10.toString());
		}
			
		if (us.isPresent()) {
			Claim cl = claimService.findByNombre(claim.getNombre());

			if (cl != null) {

				Optional<UserClaims> uc = userClaimService.findClaimForUser(us.get(), cl);

				if (!uc.isPresent()) {

					UserClaims userCl = new UserClaims();
					userCl.setClaim(cl);
					userCl.setUsuario(us.get());
					userCl.setClaimValue(claim.getValor());

					userClaimService.save(userCl);

					return ResponseEntity.ok(JWTMessages.CLAIM_AGREGADA.toString());
				} else {
					return ResponseEntity.ok(SSOError.CLAIM_DUPLICADA.toString());
				}

			} else {
				return ResponseEntity.ok(SSOError.CLAIM_NO_VALIDA.toString());

			}
		}
		
		LOG.warn(SSOError.USUARIO_INVALIDO.toString());
		return ResponseEntity.ok(SSOError.USUARIO_INVALIDO.toString());
	}
}
