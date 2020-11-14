package com.sso.springboot.Tenant;

import org.springframework.http.ResponseEntity;

public  interface TenantService {
	public void save(Tenant tenant);
	public Tenant findByApikey(String apiKey);
	public Tenant findByApiName(String tenant);
	public ResponseEntity<Void> update(Tenant tenantExistente, Tenant tenantModificado);
}
