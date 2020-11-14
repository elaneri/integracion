package com.sso.springboot.Tenant;

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

import com.sso.springboot.Messages.SSOMessages;

@RestController
@RequestMapping("/Tenants")
public class TenantController {

	@Autowired
	private TenantServiceImpl tenantService;

	private static final Logger LOG = LoggerFactory.getLogger(TenantController.class);

	// PUT: http://localhost:8080/Tenants/callbacks
	@RequestMapping(value = "/{callbacks}", method = RequestMethod.PUT)
	public ResponseEntity<Void> actualizarCallbacksTenant(@RequestHeader("x-api-key") String apk,
			 @RequestBody Tenant tenantModificado) throws ResponseStatusException {

		try{
			Tenant tenantExistente = tenantService.findByApikey(apk);
			if (tenantExistente == null) {
				LOG.warn(SSOMessages.API_KEY_INCORRECTA.toString());
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
								SSOMessages.API_KEY_INCORRECTA.toString());
			}

			tenantService.update(tenantExistente, tenantModificado);
			return ResponseEntity.ok(null);
		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getReason());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}
	}
}
