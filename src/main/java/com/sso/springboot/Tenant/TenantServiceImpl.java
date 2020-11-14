package com.sso.springboot.Tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class TenantServiceImpl implements TenantService{
	
	@Autowired
	TenantDAO tenantDAO;
	
	@Override
	public void save(Tenant tenant) {
		 tenantDAO.save(tenant);
	}

	@Override
	public Tenant findByApikey(String apiKey) {

		return tenantDAO.findByApyKey(apiKey);
	}

	@Override
	public Tenant findByApiName(String nombre) {
		// TODO Auto-generated method stub
		return tenantDAO.findByApyName(nombre);
	}

	@Override
	public ResponseEntity<Void> update(Tenant tenantExistente, Tenant tenantModificado) {
		tenantExistente.setCallbackSuccess(tenantModificado.getCallbackSuccess().trim());
		tenantExistente.setCallbackError(tenantModificado.getCallbackError().trim());
		tenantDAO.save(tenantExistente);
		return ResponseEntity.ok(null);
	}
}
