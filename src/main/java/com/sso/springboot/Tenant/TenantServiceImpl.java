package com.sso.springboot.Tenant;

import org.springframework.beans.factory.annotation.Autowired;
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

}
