package com.sso.springboot.UserClaims;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.sso.springboot.Messages.JWTError;
import com.sso.springboot.Messages.JWTMessages;
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
			throw new Exception("El tamaño del valor del claim no puede superar los 10 caracteres"); 
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
					return ResponseEntity.ok(JWTError.CLAIM_DUPLICADA.toString());
				}

			} else {
				return ResponseEntity.ok(JWTError.CLAIM_NO_VALIDA.toString());

			}

		}
		return ResponseEntity.ok(JWTError.USUARIO_INVALIDO.toString());
	}
}
