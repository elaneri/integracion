package com.sso.springboot.UserClaims;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Claims.ClaimsServiceImpl;
import com.sso.springboot.Messages.SSOMessages;
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
	public ResponseEntity<List<Claim>> getValidClaims() {
		List<Claim> claims = claimService.getValidClaims();
		return ResponseEntity.ok(claims);
	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserClaims>> getUserClaims(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws ResponseStatusException {

		try {

			Optional<Usuario> us = usuarioService.findByUserIdAndTenant(String.valueOf(idUsuario), apk);
			List<UserClaims> cls = new ArrayList<>();
			if (us != null) {

				cls = userClaimService.findClaimsForUser(us.get());
				return ResponseEntity.ok(cls);

			} else {

				LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());

			}

		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getReason());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}

	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserClaims> deleteClaim(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody RequestClaim claim) throws ResponseStatusException {
		try {

			Optional<Usuario> us = usuarioService.findByUserIdAndTenant(String.valueOf(idUsuario), apk);

			if (us != null) {

				List<UserClaims> claims = new ArrayList<UserClaims>();
				claims = userClaimService.findClaimsForUser(us.get());

				Claim cl = claimService.findByNombre(claim.getNombre());
				UserClaims userClaim = null;

				if (cl != null) {

					for (UserClaims userClaims : claims) {

						String usc = userClaims.getClaim().getNombre();
						if (cl.getNombre().equals(usc)) {
							userClaims.setClaimValue(claim.getValor());
							userClaim = userClaims;
						}
					}

					if (userClaim != null) {
						userClaimService.delete(userClaim);
						return ResponseEntity.ok(userClaim);

					} else {
						LOG.warn(SSOMessages.CLAIM_NO_VALIDA.toString());
						throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.CLAIM_NO_VALIDA.toString());
					}

				} else {
					LOG.warn(SSOMessages.CLAIM_NO_VALIDA.toString());
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.CLAIM_NO_VALIDA.toString());

				}

			} else {
				LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());

			}
		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getReason());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}

	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserClaims> updateClaim(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody RequestClaim claim) throws Exception {
		try {
			Optional<Usuario> us = usuarioService.findByUserIdAndTenant(String.valueOf(idUsuario), apk);

			if (claim.getValor().length() > 10) {
				LOG.warn(SSOMessages.CLAIM_VALOR_10_CARACTERES.toString());
				throw new ResponseStatusException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
						SSOMessages.CLAIM_VALOR_10_CARACTERES.toString());
			}

			if (us != null) {

				List<UserClaims> claims = new ArrayList<UserClaims>();
				claims = userClaimService.findClaimsForUser(us.get());

				Claim cl = claimService.findByNombre(claim.getNombre());
				UserClaims userClaim = null;

				if (cl != null) {

					for (UserClaims userClaims : claims) {

						String usc = userClaims.getClaim().getNombre();
						if (cl.getNombre().equals(usc)) {
							userClaims.setClaimValue(claim.getValor());
							userClaim = userClaims;
						}
					}

					if (userClaim != null) {
						userClaimService.save(userClaim);
						return ResponseEntity.ok(userClaim);

					} else {
						LOG.warn(SSOMessages.CLAIM_NO_VALIDA.toString());
						throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.CLAIM_NO_VALIDA.toString());
					}

				} else {
					LOG.warn(SSOMessages.CLAIM_NO_VALIDA.toString());
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.CLAIM_NO_VALIDA.toString());

				}

			} else {
				LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());

			}

		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getReason());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}

	}

	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserClaims> addClaim(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody RequestClaim claim) throws Exception {
		try {
		Optional<Usuario> us = usuarioService.findByUserIdAndTenant(String.valueOf(idUsuario), apk);

		if (claim.getValor().length() > 10) {
			LOG.warn(SSOMessages.CLAIM_VALOR_10_CARACTERES.toString());
			throw new ResponseStatusException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
					SSOMessages.CLAIM_VALOR_10_CARACTERES.toString());
		}

		if (us != null) {

			List<UserClaims> claims = new ArrayList<UserClaims>();
			claims = userClaimService.findClaimsForUser(us.get());

			if (!claims.isEmpty() && claims.size() > 10) {
				LOG.warn(SSOMessages.CLAIM_CANTIDAD_10.toString());
				throw new ResponseStatusException(HttpStatus.INSUFFICIENT_STORAGE,
						SSOMessages.CLAIM_CANTIDAD_10.toString());
			}

			Claim cl = claimService.findByNombre(claim.getNombre());

			if (cl != null) {

				Optional<UserClaims> uc = userClaimService.findClaimForUser(us.get(), cl);

				if (!uc.isPresent()) {

					UserClaims userCl = new UserClaims();
					userCl.setClaim(cl);
					userCl.setUsuario(us.get());
					userCl.setClaimValue(claim.getValor());

					userClaimService.save(userCl);

					return ResponseEntity.ok(userCl);
				} else {
					
					LOG.warn(SSOMessages.CLAIM_DUPLICADA.toString());
					throw new ResponseStatusException(HttpStatus.CONFLICT, SSOMessages.CLAIM_DUPLICADA.toString());

				}

			} else {
				LOG.warn(SSOMessages.CLAIM_NO_VALIDA.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.CLAIM_NO_VALIDA.toString());

			}
		}else{
			LOG.warn(SSOMessages.USUARIO_INVALIDO.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());
		}

		
		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getReason());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}
	}
}
